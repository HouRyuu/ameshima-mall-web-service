package com.tmall.order.entity.vo;

import java.util.List;

public class OrderDetailVO {

    private String parentOrderNo;
    private String orderNo;
    private Integer storeId;
    private String storeName;
    private Short orderState;
    private List<OrderLogisticsGoodsVO> logisticsGoodsList;
    private OrderPayVO orderPay;

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Short getOrderState() {
        return orderState;
    }

    public void setOrderState(Short orderState) {
        this.orderState = orderState;
    }

    public List<OrderLogisticsGoodsVO> getLogisticsGoodsList() {
        return logisticsGoodsList;
    }

    public void setLogisticsGoodsList(List<OrderLogisticsGoodsVO> logisticsGoodsList) {
        this.logisticsGoodsList = logisticsGoodsList;
    }

    public OrderPayVO getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(OrderPayVO orderPay) {
        this.orderPay = orderPay;
    }
}
