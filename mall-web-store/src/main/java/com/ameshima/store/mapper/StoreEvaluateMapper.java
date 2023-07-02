package com.ameshima.store.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.store.entity.dto.StoreEvaluateDTO;
import com.ameshima.store.entity.po.StoreEvaluatePO;
import org.apache.ibatis.annotations.Param;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreEvaluateMapper extends BaseMapper<StoreEvaluatePO> {

    StoreEvaluateDTO getStoreEvaluate(@Param("storeId") int storeId);

}
