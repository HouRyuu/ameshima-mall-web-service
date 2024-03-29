package com.ameshima.order.entity.po;

import com.ameshima.common.po.BasePO;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "t_order_goods")
public class OrderGoodsPO extends BasePO {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String parentOrderNo;
    private String orderNo;
    private Integer accountId;
    private Integer storeId;
    private String storeName;
    private Integer goodsId;
    private String imgUrl;
    private String goodsName;
    private Integer skuId;
    private String attrsJson;
    private BigDecimal price;
    private BigDecimal marketPrice;
    private String goodsLocation;
    private Integer orderNum;
    private BigDecimal freight;
    private Short orderState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getAttrsJson() {
        return attrsJson;
    }

    public void setAttrsJson(String attrsJson) {
        this.attrsJson = attrsJson;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getGoodsLocation() {
        return goodsLocation;
    }

    public void setGoodsLocation(String goodsLocation) {
        this.goodsLocation = goodsLocation;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Short getOrderState() {
        return orderState;
    }

    public void setOrderState(Short orderState) {
        this.orderState = orderState;
    }
}
