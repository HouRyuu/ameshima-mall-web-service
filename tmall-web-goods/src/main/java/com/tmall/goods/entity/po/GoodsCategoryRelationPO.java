package com.tmall.goods.entity.po;


import com.tmall.common.po.BasePO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_goods_category_relation")
public class GoodsCategoryRelationPO {

    @Id
    private Integer goodsId;
    private Integer categoryId;

    public GoodsCategoryRelationPO() {
    }

    public GoodsCategoryRelationPO(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
