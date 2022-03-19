package com.tmall.store.entity.po;

import com.tmall.common.po.BasePO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "t_order_logistics")
public class OrderLogisticsPO extends BasePO {

    @Id
    private String orderNo;
    private String targetAddress;
    private String logisticsCompany;
    private String trackingNo;
    private Short logisticsState;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
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
