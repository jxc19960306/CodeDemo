package com.jxc.exampleDemo.elasticSearch;


import java.util.List;
import java.util.Map;

/**
 * ItemSearchService接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-07-11<p>
 */
public interface ItemSearchService {


    /** 商品搜索方法 */
    Map<String,Object> search(Map<String, Object> params);

    /** 添加或修改商品的索引 */
    void saveOrUpdate(List<EsItem> esItems);

    /** 删除商品的索引 */
    void delete(List<Long> goodsIds);
}
