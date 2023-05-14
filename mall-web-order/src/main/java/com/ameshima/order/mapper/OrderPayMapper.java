package com.ameshima.order.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.order.entity.po.OrderPayPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface OrderPayMapper extends BaseMapper<OrderPayPO> {

    int payByParentOrderNo(OrderPayPO orderPay);

}
