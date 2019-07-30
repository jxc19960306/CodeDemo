package com.jxc.exampleDemo.elasticSearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * EsItemDao接口
 *
 * @author lee.siu.wah
 * @version 1.0
 * <p>File Created at 2019-07-11<p>
 */
public interface EsItemDao extends ElasticsearchRepository<EsItem, Long> {


}
