package com.tmall.goods.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.tmall.common.dto.PublicResult;
import com.tmall.goods.entity.po.GoodsCategoryRelationPO;
import com.tmall.goods.mapper.GoodsCategoryRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.tmall.goods.entity.dto.GoodsCategoryDTO;
import com.tmall.goods.mapper.GoodsCategoryMapper;
import com.tmall.goods.service.GoodsCategoryService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

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

    @Resource
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource
    private GoodsCategoryRelationMapper categoryRelationMapper;

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

    @Override
    public PublicResult<?> saveGoodsCategoryRelation(int goodsId, int categoryId) {
        GoodsCategoryRelationPO relationPO = new GoodsCategoryRelationPO(goodsId);
        relationPO = categoryRelationMapper.selectOne(relationPO);
        if (relationPO == null) {
            relationPO = new GoodsCategoryRelationPO(goodsId);
            relationPO.setCategoryId(categoryId);
            categoryRelationMapper.insert(relationPO);
            return PublicResult.success();
        }
        if (Objects.equals(categoryId, relationPO.getCategoryId())) {
            return PublicResult.success();
        }
        relationPO.setCategoryId(categoryId);
        Example example = new Example(GoodsCategoryRelationPO.class);
        example.and().andEqualTo("goodsId", goodsId);
        categoryRelationMapper.updateByExample(relationPO, example);
        return PublicResult.success();

    }
}
