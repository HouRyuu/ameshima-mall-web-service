package com.tmall.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimaps;
import com.tmall.common.redis.RedisClient;
import com.tmall.goods.entity.dto.GoodsAttrDTO;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsAttrMapper;
import com.tmall.goods.service.GoodsAttrService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class GoodsAttrServiceImpl implements GoodsAttrService {

    @Autowired
    private GoodsAttrMapper goodsAttrMapper;
    @Autowired
    private RedisClient redisClient;

    @Override
    public Map<String, Collection<GoodsAttrDTO>> findGoodsAttrList(int goodsId) {
        return // redisClient.get(GoodsKey.GOODS_ATTRS, goodsId,
                // () ->
        converAttrs(goodsAttrMapper.findGoodsAttrList(goodsId));
    }

    private Map<String, Collection<GoodsAttrDTO>> converAttrs(List<GoodsAttrDTO> goodsAttrList) {
        return Multimaps.index(goodsAttrList, GoodsAttrDTO::getName).asMap();
    }
}
