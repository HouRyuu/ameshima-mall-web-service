package com.tmall.order.utils;

import com.google.common.collect.Lists;
import com.tmall.common.constants.TmallConstant;
import com.tmall.order.entity.po.OrderGoodsPO;
import com.tmall.order.entity.po.OrderLogisticsPO;
import com.tmall.order.entity.po.OrderPayPO;
import com.tmall.order.entity.vo.OrderDetailVO;
import com.tmall.order.entity.vo.OrderLogisticsGoodsVO;
import com.tmall.order.entity.vo.OrderLogisticsVO;
import com.tmall.order.entity.vo.OrderPayVO;
import com.tmall.remote.goods.dto.CartGoodsDTO;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class ConvertToVO {

    public static List<OrderDetailVO> fromPO(List<OrderGoodsPO> goodsList, List<OrderPayPO> payList, List<OrderLogisticsPO> logisticsList) {
        Map<String, OrderPayVO> payMap = payList.stream().collect(Collectors.toMap(OrderPayPO::getOrderNo, payPo -> {
            OrderPayVO payVO = new OrderPayVO();
            payVO.setOrderNo(payPo.getOrderNo());
            payVO.setPayNo(payPo.getPayNo());
            payVO.setDealPrice(payPo.getDealPrice());
            payVO.setPayWay(payPo.getPayWay());
            payVO.setPayState(payPo.getPayState());
            return payVO;
        }));
        Map<String, OrderLogisticsVO> logisticsMap = logisticsList.stream().collect(Collectors.toMap(logisticsPO -> logisticsPO.getOrderNo() + TmallConstant.UNDERLINE + logisticsPO.getGoodsLocation(), logisticsPO -> {
            OrderLogisticsVO logisticsVO = new OrderLogisticsVO();
            logisticsVO.setOrderNo(logisticsPO.getOrderNo());
            logisticsVO.setGoodsLocation(logisticsPO.getGoodsLocation());
            logisticsVO.setTargetAddress(logisticsPO.getTargetAddress());
            logisticsVO.setTrackingNo(logisticsPO.getTrackingNo());
            logisticsVO.setLogisticsState(logisticsPO.getLogisticsState());
            return logisticsVO;
        }));
        List<OrderDetailVO> orderList = new ArrayList<>(goodsList.stream().collect(Collectors.toMap(OrderGoodsPO::getOrderNo, goodsPO -> {
            OrderDetailVO detail = new OrderDetailVO();
            detail.setParentOrderNo(goodsPO.getParentOrderNo());
            detail.setOrderNo(goodsPO.getOrderNo());
            detail.setStoreId(goodsPO.getStoreId());
            detail.setStoreName(goodsPO.getStoreName());
            detail.setOrderState(goodsPO.getOrderState());
            detail.setOrderPay(payMap.get(goodsPO.getOrderNo()));
            CartGoodsDTO goods = new CartGoodsDTO();
            BeanUtils.copyProperties(goodsPO, goods);
            goods.setName(goodsPO.getGoodsName());
            goods.setAmount(goodsPO.getOrderNum());
            goods.setLocation(goodsPO.getGoodsLocation());
            OrderLogisticsGoodsVO logisticsGoods = new OrderLogisticsGoodsVO();
            logisticsGoods.setOrderLogistics(logisticsMap.get(goodsPO.getOrderNo() + TmallConstant.UNDERLINE + goodsPO.getGoodsLocation()));
            logisticsGoods.setGoodsList(Lists.newArrayList(goods));
            detail.setLogisticsGoodsList(Lists.newArrayList(logisticsGoods));
            return detail;
        }, (oldGoods, newGoods) -> {
            List<OrderLogisticsGoodsVO> oldLogisticsGoodsList = oldGoods.getLogisticsGoodsList();
            List<OrderLogisticsGoodsVO> newLogisticsGoodsList = newGoods.getLogisticsGoodsList();
            boolean logisticsFlag = false;
            for (OrderLogisticsGoodsVO logisticsGoods : oldLogisticsGoodsList) {
                if (logisticsGoods.getOrderLogistics().equals(newLogisticsGoodsList.get(0).getOrderLogistics())) {
                    logisticsFlag = true;
                    logisticsGoods.getGoodsList().addAll(newLogisticsGoodsList.get(0).getGoodsList());
                }
            }
            if (!logisticsFlag) {
                oldGoods.getLogisticsGoodsList().addAll(newGoods.getLogisticsGoodsList());
            }
            return oldGoods;
        }, TreeMap::new)).values());
        if (orderList.size() > 1) {
            orderList.sort(Comparator.comparing(OrderDetailVO::getParentOrderNo).reversed().thenComparing(OrderDetailVO::getOrderNo));
        }
        return orderList;
    }


}
