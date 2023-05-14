package com.ameshima.goods.mapper;

import java.util.List;

import com.ameshima.goods.entity.dto.GoodsPromoteDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsPromoteMapper {

    List<GoodsPromoteDTO> findPromotes(GoodsPromoteDTO queryParam);

}
