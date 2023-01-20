package com.tmall.goods.entity.dto;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class GoodsCategoryDTO {

    private int id;
    private int level;
    private int secondLevel;
    private String name;
    private Integer pid;
    private List<GoodsCategoryDTO> categoryList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSecondLevel() {
        return secondLevel;
    }

    public void setSecondLevel(int secondLevel) {
        this.secondLevel = secondLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<GoodsCategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<GoodsCategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }
}
