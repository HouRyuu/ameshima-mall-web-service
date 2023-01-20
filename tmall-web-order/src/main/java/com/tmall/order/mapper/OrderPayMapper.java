package com.tmall.order.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.order.entity.po.OrderPayPO;

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
