package com.tmall.goods.entity.po;

import com.tmall.common.po.BasePO;
import tk.mybatis.mapper.annotation.KeySql;

import javax.annotation.Generated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_goods_param")
public class GoodsParamPO extends BasePO {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private Integer goodsId;
    private String paramName;
    private String paramValue;

    public GoodsParamPO() {
    }

    public GoodsParamPO(Integer id, Integer goodsId, String paramName, String paramValue) {
        this.id = id;
        this.goodsId = goodsId;
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
