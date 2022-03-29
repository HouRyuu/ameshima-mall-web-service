package com.tmall.remote.goods.vo;

import java.util.List;

import com.tmall.remote.goods.dto.CartGoodsDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ShopCartVO {

    private Integer storeId;
    private String storeName;
    private List<CartGoodsDTO> goodsList;
    private Integer goodsState;
    private String orderNo;

    public ShopCartVO() {
    }

    public ShopCartVO(Integer storeId, String storeName, Integer goodsState) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.goodsState = goodsState;
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

    public List<CartGoodsDTO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartGoodsDTO> goodsList) {
        this.goodsList = goodsList;
    }

    public Integer getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(Integer goodsState) {
        this.goodsState = goodsState;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ShopCartVO that = (ShopCartVO) o;

        return storeId.equals(that.storeId) && goodsState.equals(that.goodsState);
    }

    @Override
    public int hashCode() {
        int result = storeId.hashCode();
        result = 31 * result + goodsState.hashCode();
        return result;
    }
}
