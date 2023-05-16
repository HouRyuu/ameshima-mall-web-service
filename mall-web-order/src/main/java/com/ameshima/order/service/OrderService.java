package com.ameshima.order.service;

import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.order.entity.dto.OrderConditionDTO;
import com.ameshima.order.entity.vo.OrderDetailVO;
import com.ameshima.order.entity.vo.OrderLogisticsVO;

import java.util.List;

public interface OrderService {

    PublicResult<?> orderTuQueue(String cityCode, String address);

    PublicResult<Integer> getOrderQueueState(String parentOrderNo);

    PublicResult<List<OrderDetailVO>> findOrderGoodsList(String parentOrderNo, short orderState);

    PublicResult<PageResult<OrderDetailVO>> orderPage(OrderConditionDTO condition);

    PublicResult<String> createPayPayCode(String parentOrderNo, String orderNo);

    PublicResult<?> getPayPayStatus(String parentOrderNo, String orderNo);

    PublicResult<?> receiveConfirm(String orderNo);

    void generateOrder(String orderStr);

    boolean orderGoodsExists(int goodsId, short status);

    PublicResult<?> delivery(OrderLogisticsVO logistics);
}
