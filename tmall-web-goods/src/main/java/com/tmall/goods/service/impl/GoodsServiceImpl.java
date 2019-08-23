package com.tmall.goods.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.redis.RedisClient;
import com.tmall.goods.entity.dto.*;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsMapper;
import com.tmall.goods.service.GoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;

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
    @Autowired
    private RedisClient redisClient;

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

    @Override
    public List<StoreGoodsDTO> storeGoods(int storeId) {
        return redisClient.get(GoodsKey.STORE_INDEX_GOODS, storeId, () -> goodsMapper.storeGoods(storeId));
    }

    @Override
    public GoodsDTO getGoods(int goodsId) {
        return goodsMapper.getGoods(goodsId);
    }

    @Override
    public List<GoodsImgDTO> findImgs(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_IMGS, goodsId, () -> goodsMapper.findImgs(goodsId));
    }

    @Override
    public List<GoodsParamDTO> findParams(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_PARAMS, goodsId, () -> goodsMapper.findParams(goodsId));
    }

    @Override
    public List<GoodsSkuDTO> findSku(int goodsId) {
        return goodsMapper.findSku(goodsId);
    }
}
