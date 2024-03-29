package com.ameshima.order.entity.po;

import com.ameshima.common.po.BasePO;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "t_order_pay")
public class OrderPayPO extends BasePO {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String orderNo;
    private Integer accountId;
    private BigDecimal dealPrice;
    private Short payWay;
    private Short payState;
    private String payNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
