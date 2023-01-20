package com.tmall.goods.entity.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class StoreGoodsDTO extends GoodsGridDTO {

    private short isShowBanner;
    private short isPromote;
    private String bannerUrl;
    private Integer categoryId2;
    private Integer categoryId3;
    private Integer categoryId4;
    private BigDecimal promoPrice;
    private String location;
    private List<GoodsImgDTO> imgList;
    private List<GoodsSkuDTO> skuList;

    public short getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(short isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    public short getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(short isPromote) {
        this.isPromote = isPromote;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Integer categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public Integer getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(Integer categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public Integer getCategoryId4() {
        return categoryId4;
    }

    public void setCategoryId4(Integer categoryId4) {
        this.categoryId4 = categoryId4;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<GoodsImgDTO> getImgList() {
        return imgList;
    }

    public void setImgList(List<GoodsImgDTO> imgList) {
        this.imgList = imgList;
    }

    public List<GoodsSkuDTO> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<GoodsSkuDTO> skuList) {
        this.skuList = skuList;
    }
}
