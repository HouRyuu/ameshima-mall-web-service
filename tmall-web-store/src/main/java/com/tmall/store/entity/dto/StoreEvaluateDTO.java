package com.tmall.store.entity.dto;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
