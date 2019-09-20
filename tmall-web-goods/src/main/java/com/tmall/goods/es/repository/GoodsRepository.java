package com.tmall.goods.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tmall.goods.entity.dto.EsGoodsDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsRepository extends ElasticsearchRepository<EsGoodsDTO, Integer> {
}
