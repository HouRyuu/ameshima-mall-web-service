package com.tmall.order.service;

import com.github.pagehelper.PageInfo;
import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.order.entity.vo.OrderEvaluateVO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface OrderEvaluateService {

    int goodsEvaluateCount(int goodsId);

    PublicResult<?> create(OrderEvaluateVO evaluate);

    PublicResult<PageResult<OrderEvaluateVO>> evaluatePage(OrderEvaluateVO condition);

}
