package com.tmall.goods.entity.dto;

import java.math.BigDecimal;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class GoodsGridDTO {

    private int id;
    private int storeId;
    private String name;
    private String simpleDesc;
    private BigDecimal price;
    private String imgUrl;
    private int categoryId;
    private Short status;

    public GoodsGridDTO() {
    }

    public GoodsGridDTO(int id, int storeId, String name, String simpleDesc, BigDecimal price, String imgUrl, Short status) {
        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this.simpleDesc = simpleDesc;
        this.price = price;
        this.imgUrl = imgUrl;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getSimpleDesc() {
        return simpleDesc;
    }

    public void setSimpleDesc(String simpleDesc) {
        this.simpleDesc = simpleDesc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }
}
