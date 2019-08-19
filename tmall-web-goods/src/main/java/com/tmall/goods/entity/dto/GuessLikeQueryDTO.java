package com.tmall.goods.entity.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
