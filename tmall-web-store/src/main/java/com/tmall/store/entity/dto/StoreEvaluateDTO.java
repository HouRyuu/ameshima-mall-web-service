package com.tmall.store.entity.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StoreEvaluateDTO {

    private int storeId;
    private String name;
    private float descScore;
    private float serviceScore;
    private float logisticsScore;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDescScore() {
        return descScore;
    }

    public void setDescScore(float descScore) {
        this.descScore = descScore;
    }

    public float getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(float serviceScore) {
        this.serviceScore = serviceScore;
    }

    public float getLogisticsScore() {
        return logisticsScore;
    }

    public void setLogisticsScore(float logisticsScore) {
        this.logisticsScore = logisticsScore;
    }
}
