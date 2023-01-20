package com.tmall.goods.entity.po;

import com.tmall.common.po.BasePO;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Table(name = "t_goods_freight")
public class GoodsFreightPO extends BasePO {

    @Id
    private Integer id;
    private Integer storeId;
    private Integer goodsId;
    private String dispatchCityCode;
    private String targetCityCode;
    private BigDecimal cost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getDispatchCityCode() {
        return dispatchCityCode;
    }

    public void setDispatchCityCode(String dispatchCityCode) {
        this.dispatchCityCode = dispatchCityCode;
    }

    public String getTargetCityCode() {
        return targetCityCode;
    }

    public void setTargetCityCode(String targetCityCode) {
        this.targetCityCode = targetCityCode;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
