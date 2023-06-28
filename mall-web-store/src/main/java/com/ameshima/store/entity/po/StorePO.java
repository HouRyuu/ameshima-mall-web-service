package com.ameshima.store.entity.po;

import com.ameshima.common.po.BasePO;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_store")
public class StorePO extends BasePO {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
    private String name;
    private Byte storeType;

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

    public Byte getStoreType() {
        return storeType;
    }

    public void setStoreType(Byte storeType) {
        this.storeType = storeType;
    }
}
