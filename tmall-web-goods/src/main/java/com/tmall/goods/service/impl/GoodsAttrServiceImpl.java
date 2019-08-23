package com.tmall.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
    public List<Map<String, Object>> findGoodsAttrList(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_ATTRS, goodsId,
                () -> converAttrs(goodsAttrMapper.findGoodsAttrList(goodsId)));
    }

    private List<Map<String, Object>> converAttrs(List<GoodsAttrDTO> goodsAttrList) {
        List<Map<String, Object>> attrList = Lists.newArrayList();
        Map<String, Collection<GoodsAttrDTO>> attrMap = Multimaps.index(goodsAttrList, GoodsAttrDTO::getName).asMap();
        Set<Map.Entry<String, Collection<GoodsAttrDTO>>> entrySet = attrMap.entrySet();
        Map<String, Object> tmp;
        for (Map.Entry<String, Collection<GoodsAttrDTO>> entry : entrySet) {
            tmp = Maps.newHashMap();
            tmp.put("key", entry.getKey());
            tmp.put("value", Lists.newArrayList(entry.getValue()));
            attrList.add(tmp);
        }
        return attrList;
    }
}
