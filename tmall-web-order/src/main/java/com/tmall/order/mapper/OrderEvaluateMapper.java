package com.tmall.order.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.order.entity.po.OrderEvaluatePO;
import com.tmall.order.entity.vo.OrderEvaluateVO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface OrderEvaluateMapper extends BaseMapper<OrderEvaluatePO> {

    List<OrderEvaluateVO> evaluateList(OrderEvaluateVO condition);

}
