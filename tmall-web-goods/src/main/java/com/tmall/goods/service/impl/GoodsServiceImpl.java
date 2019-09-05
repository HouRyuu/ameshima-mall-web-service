package com.tmall.goods.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.tmall.remote.order.api.IOrderEvaluateService;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private IOrderEvaluateService orderEvaluateService;

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
        GoodsDTO result = goodsMapper.getGoods(goodsId);
        int evaluateCount = 0;
        try {
            evaluateCount = orderEvaluateService.count(goodsId);
        } catch (Exception e) {
            LOGGER.error("Get goods's evaluate count fail, goodsId=>{}", goodsId, e);
        }
        result.setEvaluateCount(evaluateCount);
        return result;
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

    @Override
    public float getFreight(int goodsId, String cityCode) {
        Float freight = goodsMapper.getFreight(goodsId, cityCode);
        return freight == null ? 0 : freight;
    }
}
