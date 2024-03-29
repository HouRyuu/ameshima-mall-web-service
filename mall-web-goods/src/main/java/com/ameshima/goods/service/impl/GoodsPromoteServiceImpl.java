package com.ameshima.goods.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ameshima.common.redis.RedisClient;
import com.ameshima.goods.entity.dto.GoodsPromoteDTO;
import com.ameshima.goods.keys.GoodsKey;
import com.ameshima.goods.mapper.GoodsPromoteMapper;
import com.ameshima.goods.service.GoodsPromoteService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class GoodsPromoteServiceImpl implements GoodsPromoteService {

    @Resource
    private GoodsPromoteMapper goodsPromoteMapper;
    @Resource
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
