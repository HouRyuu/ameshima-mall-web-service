package com.ameshima.store.entity.po;

import com.ameshima.common.po.BasePO;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "t_store_evaluate")
public class StoreEvaluatePO extends BasePO {

    @Id
    private Integer storeId;
    private Float descScore;
    private Float serviceScore;
    private Float logisticsScore;

    public StoreEvaluatePO() {
    }

    public StoreEvaluatePO(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Float getDescScore() {
        return descScore;
    }

    public void setDescScore(Float descScore) {
        this.descScore = descScore;
    }

    public Float getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(Float serviceScore) {
        this.serviceScore = serviceScore;
    }

    public Float getLogisticsScore() {
        return logisticsScore;
    }

    public void setLogisticsScore(Float logisticsScore) {
        this.logisticsScore = logisticsScore;
    }
}
