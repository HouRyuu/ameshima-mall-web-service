package com.tmall.order.service;

import com.tmall.common.dto.PublicResult;

public interface OrderService {

    PublicResult<?> orderTuQueue(String cityCode, String address);

    PublicResult<Integer> getOrderQueueState(String parentOrderNo);

    void generateOrder(String orderStr);
}
