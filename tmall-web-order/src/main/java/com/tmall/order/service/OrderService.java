package com.tmall.order.service;

import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.order.entity.dto.OrderConditionDTO;
import com.tmall.order.entity.vo.OrderDetailVO;

import java.util.List;

public interface OrderService {

    PublicResult<?> orderTuQueue(String cityCode, String address);

    PublicResult<Integer> getOrderQueueState(String parentOrderNo);

    PublicResult<List<OrderDetailVO>> findOrderGoodsList(String parentOrderNo, short orderState);

    PublicResult<PageResult<OrderDetailVO>> orderPage(OrderConditionDTO condition);

    PublicResult<?> receiveConfirm(String orderNo);

    void generateOrder(String orderStr);
}
