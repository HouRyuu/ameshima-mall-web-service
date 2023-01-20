package com.tmall.store.mapper;

import org.apache.ibatis.annotations.Param;

import com.tmall.store.entity.dto.StoreEvaluateDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreEvaluateMapper {

    StoreEvaluateDTO getStoreEvaluate(@Param("storeId") int storeId);

}
