package com.ameshima.goods.service;

import java.util.List;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.goods.entity.dto.GoodsCategoryDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsCategoryService {

    List<GoodsCategoryDTO> findSecondCategories();

    List<GoodsCategoryDTO> findChildrenCategories(int pid);

    List<GoodsCategoryDTO> findCategoriesByPid(int pid);

    PublicResult<?> saveGoodsCategoryRelation(int goodsId, int categoryId);
}
