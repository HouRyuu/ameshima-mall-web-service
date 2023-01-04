package com.tmall.goods.entity.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StoreGoodsDTO extends GoodsGridDTO {

    private short isShowBanner;
    private short isPromote;
    private String bannerUrl;
    private Integer categoryId2;
    private Integer categoryId3;
    private Integer categoryId4;
    private BigDecimal promoPrice;
    private List<GoodsImgDTO> imgList;


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

    public List<GoodsImgDTO> getImgList() {
        return imgList;
    }

    public void setImgList(List<GoodsImgDTO> imgList) {
        this.imgList = imgList;
    }
}
