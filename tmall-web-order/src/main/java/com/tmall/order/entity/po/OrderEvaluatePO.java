package com.tmall.order.entity.po;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tmall.common.po.BasePO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Table(name = "t_order_evaluate")
public class OrderEvaluatePO extends BasePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Integer goodsId;
    private Float descScore;
    private String evaluateText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Float getDescScore() {
        return descScore;
    }

    public void setDescScore(Float descScore) {
        this.descScore = descScore;
    }

    public String getEvaluateText() {
        return evaluateText;
    }

    public void setEvaluateText(String evaluateText) {
        this.evaluateText = evaluateText;
    }
}
