package com.ameshima.goods.entity.po;


import com.ameshima.common.po.BasePO;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_goods_attr")
public class GoodsAttrPO extends BasePO {

    @Id
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
