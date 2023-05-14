package com.ameshima.goods.es.repository;

import com.ameshima.goods.entity.dto.EsGoodsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

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
