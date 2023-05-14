package com.ameshima.order.service;

import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.order.entity.vo.OrderEvaluateVO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface OrderEvaluateService {

    int goodsEvaluateCount(int goodsId);

    PublicResult<?> create(OrderEvaluateVO evaluate);

    PublicResult<PageResult<OrderEvaluateVO>> evaluatePage(OrderEvaluateVO condition);

}
