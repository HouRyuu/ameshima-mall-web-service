package com.tmall.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.goods.entity.dto.GoodsGridDTO;
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

    @Override
    public List<GoodsGridDTO> findPromote(int promoteId) {
        return goodsMapper.findPromote(promoteId);
    }
}
