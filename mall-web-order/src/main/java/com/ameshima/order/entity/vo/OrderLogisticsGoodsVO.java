package com.ameshima.order.entity.vo;

import com.ameshima.remote.goods.dto.CartGoodsDTO;

import java.util.List;

public class OrderLogisticsGoodsVO {

    private OrderLogisticsVO orderLogistics;
    private List<CartGoodsDTO> goodsList;

    public OrderLogisticsVO getOrderLogistics() {
        return orderLogistics;
    }

    public void setOrderLogistics(OrderLogisticsVO orderLogistics) {
        this.orderLogistics = orderLogistics;
    }

    public List<CartGoodsDTO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartGoodsDTO> goodsList) {
        this.goodsList = goodsList;
    }
}
