package com.ameshima.order.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.order.entity.dto.OrderConditionDTO;
import com.ameshima.order.entity.po.OrderGoodsPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface OrderGoodsMapper extends BaseMapper<OrderGoodsPO> {

    List<String> parentNoList(OrderConditionDTO condition);

    OrderGoodsPO getPayInfo(@Param("payState") short payState,
                           @Param("parentNo") String parentNo,
                           @Param("orderNo") String orderNo);

}
