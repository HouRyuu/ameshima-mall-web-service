package com.tmall.order.entity.dto;

import java.util.Date;

public class OrderConditionDTO {

    private Integer accountId;
    private String parentOrderNo;
    private String goodsName;
    private Short orderState;
    private Date startDate;
    private Date endDate;
    private Integer pageIndex;
    private Integer pageSize;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Short getOrderState() {
        return orderState;
    }

    public void setOrderState(Short orderState) {
        this.orderState = orderState;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
