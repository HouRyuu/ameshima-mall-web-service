package com.tmall.goods.es.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.tmall.goods.entity.dto.EsGoodsDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsRepository extends ElasticsearchRepository<EsGoodsDTO, Integer> {
}
