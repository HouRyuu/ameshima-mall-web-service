package com.ameshima.order.entity.vo;

public class OrderLogisticsVO {

    private String orderNo;
    private String goodsLocation;
    private String targetAddress;
    private Short logisticsCompany;
    private String trackingNo;
    private Short logisticsState;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getGoodsLocation() {
        return goodsLocation;
    }

    public void setGoodsLocation(String goodsLocation) {
        this.goodsLocation = goodsLocation;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public Short getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(Short logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public Short getLogisticsState() {
        return logisticsState;
    }

    public void setLogisticsState(Short logisticsState) {
        this.logisticsState = logisticsState;
    }
}
