package com.tmall.order.entity.vo;

import java.math.BigDecimal;

public class OrderPayVO {

    private String orderNo;
    private BigDecimal dealPrice;
    private Short payWay;
    private Short payState;
    private String payNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(BigDecimal dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public Short getPayState() {
        return payState;
    }

    public void setPayState(Short payState) {
        this.payState = payState;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }
}
