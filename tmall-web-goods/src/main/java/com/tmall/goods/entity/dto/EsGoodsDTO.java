package com.tmall.goods.entity.dto;

import java.math.BigDecimal;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Document(indexName = "tmall", type = "goods")
public class EsGoodsDTO {

    @Id
    private Integer id;
    @Field(index = FieldIndex.not_analyzed)
    private String categoryIds;
    private String categories;
    private String allCategories;
    private String store;
    private String brand;
    private String name;
    @Field(index = FieldIndex.not_analyzed)
    private BigDecimal price;
    @Field(index = FieldIndex.not_analyzed)
    private String imgUrl;
    @Field(index = FieldIndex.not_analyzed)
    private int salesVolume;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(String allCategories) {
        this.allCategories = allCategories;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }
}
