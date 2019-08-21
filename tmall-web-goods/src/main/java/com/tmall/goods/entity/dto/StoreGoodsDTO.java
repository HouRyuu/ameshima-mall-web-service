package com.tmall.goods.entity.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StoreGoodsDTO extends GoodsGridDTO {

    private int isShowBanner;
    private int isPromote;
    private String bannerUrl;

    public int getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(int isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    public int getIsPromote() {
        return isPromote;
    }

    public void setIsPromote(int isPromote) {
        this.isPromote = isPromote;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
}
