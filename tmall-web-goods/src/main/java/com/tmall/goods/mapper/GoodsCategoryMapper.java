package com.tmall.goods.mapper;

import java.util.List;

import com.tmall.goods.entity.dto.GoodsCategoryDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsCategoryMapper {

    List<GoodsCategoryDTO> findCategories(GoodsCategoryDTO category);

    List<GoodsCategoryDTO> findTowLevelChildren(GoodsCategoryDTO category);
}
