package com.tmall.goods.entity.dto;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class GoodsAttrMapDTO {

    private Integer id;
    private Integer goodsId;
    private Integer attrId;
    private String attrName;
    private String txtValue;
    private String imgValue;

    public GoodsAttrMapDTO() {
    }

    public GoodsAttrMapDTO(Integer attrId, String attrName) {
        this.attrId = attrId;
        this.attrName = attrName;
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

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getTxtValue() {
        return txtValue;
    }

    public void setTxtValue(String txtValue) {
        this.txtValue = txtValue;
    }

    public String getImgValue() {
        return imgValue;
    }

    public void setImgValue(String imgValue) {
        this.imgValue = imgValue;
    }
}
