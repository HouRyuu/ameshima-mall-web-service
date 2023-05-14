package com.ameshima.order.entity.dto;

import com.ameshima.remote.goods.vo.ShopCartVO;

import java.util.List;

public class OrderMQDTO {

    private Integer accountId;
    private String address;
    private String parentOrderNo;
    private List<ShopCartVO> storeGoodsList;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentOrderNo() {
        return parentOrderNo;
    }

    public void setParentOrderNo(String parentOrderNo) {
        this.parentOrderNo = parentOrderNo;
    }

    public List<ShopCartVO> getStoreGoodsList() {
        return storeGoodsList;
    }

    public void setStoreGoodsList(List<ShopCartVO> storeGoodsList) {
        this.storeGoodsList = storeGoodsList;
    }
}
