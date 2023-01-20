package com.tmall.goods.entity.dto;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class GuessLikeQueryDTO {

    private int count;
    private int[] categories;
    private int[] notCategories;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int[] getCategories() {
        return categories;
    }

    public void setCategories(int[] categories) {
        this.categories = categories;
    }

    public int[] getNotCategories() {
        return notCategories;
    }

    public void setNotCategories(int[] notCategories) {
        this.notCategories = notCategories;
    }
}
