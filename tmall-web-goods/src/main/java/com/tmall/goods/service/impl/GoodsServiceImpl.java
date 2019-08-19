package com.tmall.goods.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.goods.entity.dto.GoodsGridDTO;
import com.tmall.goods.entity.dto.GuessLikeQueryDTO;
import com.tmall.goods.mapper.GoodsMapper;
import com.tmall.goods.service.GoodsService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public List<GoodsGridDTO> findByPromote(int promoteId) {
        return goodsMapper.findPromote(promoteId);
    }

    @Override
    public List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam) {
        int count = Integer.parseInt(globalConfig.get(GlobalConfig.INDEX_GUESS_LIKE_COUNT));
        queryParam.setCount(count);
        List<GoodsGridDTO> goodsList = ArrayUtils.isEmpty(queryParam.getCategories()) ? Lists.newArrayList()
                : goodsMapper.findByCategories(queryParam);
        if (goodsList.size() < count) {
            queryParam.setCount(count - goodsList.size());
            if (goodsList.size() > 0) {
                queryParam.setNotCategories(queryParam.getCategories());
            }
            queryParam.setCategories(null);
            goodsList.addAll(goodsMapper.findByCategories(queryParam));
        }
        return goodsList;
    }
}
