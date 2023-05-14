package com.ameshima.goods.entity.po;

import com.ameshima.common.po.BasePO;

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
@Table(name = "t_goods_sku")
public class GoodsSkuPO extends BasePO {

    @Id
    private Integer id;
    private Integer goodsId;
    private String attrs;
    private BigDecimal price;
    private BigDecimal marketPrice;
    private Integer quantity;

    public GoodsSkuPO() {
    }

    public GoodsSkuPO(Integer id, Integer goodsId, String attrs, BigDecimal price, BigDecimal marketPrice, Integer quantity) {
        this.id = id;
        this.goodsId = goodsId;
        this.attrs = attrs;
        this.price = price;
        this.marketPrice = marketPrice;
        this.quantity = quantity;
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

    public String getAttrs() {
        return attrs;
    }

    public void setAttrs(String attrs) {
        this.attrs = attrs;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
