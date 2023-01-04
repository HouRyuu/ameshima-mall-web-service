package com.tmall.goods.entity.dto;

public class GoodsQueryDTO {

    private int storeId;
    private short isStoreIndex;
    private short isShowBanner;
    private short isPromote;
    private String goodsName;
    private Integer pageIndex;
    private Integer pageSize;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public short getIsStoreIndex() {
        return isStoreIndex;
    }

    public void setIsStoreIndex(short isStoreIndex) {
        this.isStoreIndex = isStoreIndex;
    }

    public short getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(short isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    public short getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(short isPromote) {
        this.isPromote = isPromote;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
