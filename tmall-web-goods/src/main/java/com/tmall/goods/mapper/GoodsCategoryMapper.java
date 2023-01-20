package com.tmall.goods.mapper;

import java.util.List;

import com.tmall.goods.entity.dto.GoodsCategoryDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsCategoryMapper {

    List<GoodsCategoryDTO> findCategories(GoodsCategoryDTO category);

    List<GoodsCategoryDTO> findTowLevelChildren(GoodsCategoryDTO category);
}
