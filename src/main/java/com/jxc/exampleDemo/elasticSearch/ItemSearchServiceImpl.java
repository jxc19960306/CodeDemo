package com.jxc.exampleDemo.elasticSearch;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索服务接口实现类
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-07-11<p>
 */
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Autowired
    private EsItemDao esItemDao;


    /** 添加或修改商品的索引 */
    @Override
    public void saveOrUpdate(List<EsItem> esItems){
        try{
            esItemDao.saveAll(esItems);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 删除商品的索引 */
    @Override
    public void delete(List<Long> goodsIds){
        try{
            // 创建删除查询对象
            DeleteQuery deleteQuery = new DeleteQuery();
            // 设置索引库
            deleteQuery.setIndex("pinyougou");
            // 设置类型
            deleteQuery.setType("item");
            // 设置删除条件
            deleteQuery.setQuery(QueryBuilders.termsQuery("goodsId", goodsIds));
            // 条件删除
            esTemplate.delete(deleteQuery);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /** 商品搜索方法 */
    @Override
    public Map<String,Object> search(Map<String, Object> params){
        try{

            // 创建原生搜索查询构建对象
            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            // 设置默认搜索全部
            builder.withQuery(QueryBuilders.matchAllQuery());

            // 获取搜索关键字
            String keywords = (String)params.get("keywords");
            // 判断关键字
            if (StringUtils.isNoneBlank(keywords)){

                /** ############## 1. 搜索条件 ################# */
                // 添加搜索条件 (keywords复制Field)
//                builder.withQuery(QueryBuilders.matchQuery("keywords", keywords));
                builder.withQuery(QueryBuilders.multiMatchQuery(keywords,
                            "title", "category", "brand", "seller"));

                /** ############## 2. 搜索高亮 ################# */
                // 2.1 创建标题的高亮字段
                HighlightBuilder.Field title = new HighlightBuilder
                        .Field("title") // 高亮字段
                        .preTags("<font color='red'>") // 高亮前缀
                        .postTags("</font>") // 高亮后缀
                        .fragmentSize(50); // 文本截断
                // 2.2 添加高亮字段
                builder.withHighlightFields(title);
            }


            /** ############# 3. 搜索过滤 ############### */
            // { "keywords": "小米", "category": "手机", "brand": "苹果",
            // "price": "1000-1500", "spec": { "网络": "电信3G", "机身内存": "128G" } }
            // 3.1 创建组合查询构建对象，用来组合过滤条件
            BoolQueryBuilder boolBuilder = new BoolQueryBuilder();

            // 3.2 组合多个过滤条件
            // 3.2.1 根据商品分类过滤 must必须
            String category = (String)params.get("category");
            if (StringUtils.isNoneBlank(category)) {
                boolBuilder.must(QueryBuilders.termQuery("category", category));
            }

            // 3.2.2 根据商品品牌过滤
            String brand = (String)params.get("brand");
            if (StringUtils.isNoneBlank(brand)) {
                boolBuilder.must(QueryBuilders.termQuery("brand", brand));
            }

            // 3.2.3 根据商品规格选项过滤(嵌套查询)
            Map<String,String> specMap = (Map<String, String>) params.get("spec");
            // { "网络": "电信3G", "机身内存": "128G" }
            if (specMap != null && specMap.size() > 0){
                // 迭代map集合的key
                for (String key : specMap.keySet()){
                    // 嵌套Field的名称
                    String field = "spec." + key + ".keyword";
                    // path : 路径
                    // QueryBuilder: 查询条件构建对象
                    // ScoreMode: 分数模式
                    boolBuilder.must(QueryBuilders.nestedQuery("spec",
                            QueryBuilders.termQuery(field, specMap.get(key)), ScoreMode.Max));
                }
            }

            // 3.2.4 根据商品价格区间过滤
            String price = (String)params.get("price");
            if (StringUtils.isNoneBlank(price)){
                // 0-500 500-1000 1000-1500 1500-2000 2000-3000 3000-*
                String[] priceArr = price.split("-");
                // 创建范围查询构建对象
                RangeQueryBuilder rqBuilder = new RangeQueryBuilder("price");
                if ("*".equals(priceArr[1])){
                    // >= 3000
                    rqBuilder.gte(priceArr[0]);
                }else {
                    // 0 <=   < 500
                    rqBuilder.from(priceArr[0], true).to(priceArr[1], false);
                }
                // 组合范围查询
                boolBuilder.must(rqBuilder);
            }

            // 3.3 添加过滤查询
            builder.withFilter(boolBuilder);


            // 创建SearchQuery搜索查询对象
            SearchQuery query = builder.build();

            /** ############# 4. 搜索分页 ############### */
            // 获取当前页码
            Integer currPage = (Integer) params.get("page");
            if (currPage == null){
                currPage = 1;
            }
            // 创建分页对象
            Pageable pageable = PageRequest.of(currPage - 1, 10);
            // 设置分页
            query.setPageable(pageable);


            /** ############# 5. 搜索排序 ############### */
            String sortField = (String)params.get("sortField");
            String sortValue = (String)params.get("sortValue");
            if (StringUtils.isNoneBlank(sortField) && StringUtils.isNoneBlank(sortValue)){
                // 创建排序对象
                Sort sort = new Sort("ASC".equals(sortValue) ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortField);
                // 添加排序对象
                query.addSort(sort);
            }


            // 分页搜索，得到合计分页对象
            AggregatedPage<EsItem> page = esTemplate.queryForPage(query, EsItem.class,
                    new SearchResultMapper() { // 对搜索结果转化(映射)
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse sr,
                                                        Class<T> aClass, Pageable pageable) {
                    // List<T> content 搜索分页数据
                    List<T> content = new ArrayList<T>();
                    // 迭代全部命中的文档
                    for (SearchHit hit : sr.getHits()) {
                        // hit 一篇文档
                        // 获取文档的json字符串，转化成EsItem
                        String sourceAsStr = hit.getSourceAsString();
                        EsItem esItem = JSON.parseObject(sourceAsStr, EsItem.class);

                        // 获取标题高亮字段
                        HighlightField titleField = hit.getHighlightFields().get("title");
                        // 判断不是空
                        if (titleField != null) {
                            // 获取标题的高亮内容
                            String title = titleField.getFragments()[0].toString();
                            esItem.setTitle(title);
                        }
                        content.add((T)esItem);
                    }
                    // Pageable pageable  分页参数封装对象
                    // long total  总记录数
                    return new AggregatedPageImpl<T>(content, pageable, sr.getHits().getTotalHits());
                }
            });

            Map<String,Object> data = new HashMap<String,Object>();
            // 总记录数
            data.put("total", page.getTotalElements());
            // 分页数据
            data.put("rows", page.getContent());
            // 总页数
            data.put("totalPages", page.getTotalPages());

            return data;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
