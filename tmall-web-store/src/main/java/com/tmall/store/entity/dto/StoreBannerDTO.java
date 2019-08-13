package com.tmall.store.entity.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class StoreBannerDTO {

    private int storeId;
    private String bannerImg;
    private String bannerColor;
    private Integer isIndexShow;
    private Integer firstIndex;
    private Integer showCount;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    public String getBannerColor() {
        return bannerColor;
    }

    public void setBannerColor(String bannerColor) {
        this.bannerColor = bannerColor;
    }

    public Integer getIsIndexShow() {
        return isIndexShow;
    }

    public void setIsIndexShow(Integer isIndexShow) {
        this.isIndexShow = isIndexShow;
    }

    public Integer getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(Integer firstIndex) {
        this.firstIndex = firstIndex;
    }

    public Integer getShowCount() {
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }
}
