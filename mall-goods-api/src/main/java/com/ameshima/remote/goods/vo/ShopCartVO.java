package com.ameshima.remote.goods.vo;

import java.util.List;

import com.ameshima.remote.goods.dto.CartGoodsDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
