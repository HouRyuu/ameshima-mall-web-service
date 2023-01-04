package com.tmall.goods.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.tmall.goods.entity.dto.GoodsCategoryDTO;
import com.tmall.goods.mapper.GoodsCategoryMapper;
import com.tmall.goods.service.GoodsCategoryService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public List<GoodsCategoryDTO> findSecondCategories() {
        GoodsCategoryDTO category = new GoodsCategoryDTO();
        category.setLevel(2);
        return goodsCategoryMapper.findCategories(category);
    }

    @Override
    public List<GoodsCategoryDTO> findChildrenCategories(int pid) {
        GoodsCategoryDTO category = new GoodsCategoryDTO();
        category.setPid(pid);
        return goodsCategoryMapper.findCategories(category);
    }

    @Override
    public List<GoodsCategoryDTO> findCategoriesByPid(int pid) {
        GoodsCategoryDTO query = new GoodsCategoryDTO();
        query.setPid(pid);
        query.setSecondLevel(2);
        List<GoodsCategoryDTO> categoryList = goodsCategoryMapper.findTowLevelChildren(query);
        if (CollectionUtils.isEmpty(categoryList)) {
            return Collections.emptyList();
        }
        List<GoodsCategoryDTO> result = Lists.newArrayList();
        GoodsCategoryDTO topCategory;
        for (GoodsCategoryDTO category : categoryList) {
            if (category.getPid() == pid) {
                result.add(category);
                continue;
            }
            topCategory = result.get(result.size() - 1);
            if (topCategory.getCategoryList() == null) {
                topCategory.setCategoryList(Lists.newArrayList());
            }
            topCategory.getCategoryList().add(category);
        }
        return result;
    }
}
