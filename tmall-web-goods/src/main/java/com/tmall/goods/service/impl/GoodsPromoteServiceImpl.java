package com.tmall.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.common.redis.RedisClient;
import com.tmall.goods.entity.dto.GoodsPromoteDTO;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsPromoteMapper;
import com.tmall.goods.service.GoodsPromoteService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class GoodsPromoteServiceImpl implements GoodsPromoteService {

    @Autowired
    private GoodsPromoteMapper goodsPromoteMapper;
    @Autowired
    private RedisClient redisClient;

    @Override
    public List<GoodsPromoteDTO> findPromotes() {
        return redisClient.get(GoodsKey.INDEX_PROMOTE_PLATE, null, () -> {
            GoodsPromoteDTO queryParam = new GoodsPromoteDTO();
            queryParam.setIsShowIndex(1);
            return goodsPromoteMapper.findPromotes(queryParam);
        });
    }
}
