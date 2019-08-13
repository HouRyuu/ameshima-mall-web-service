package com.tmall.goods.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.redis.RedisClient;
import com.tmall.goods.entity.dto.GoodsBrandDTO;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsBrandMapper;
import com.tmall.goods.service.GoodsBrandService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class GoodsBrandServiceImpl implements GoodsBrandService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private GoodsBrandMapper goodsBrandMapper;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public List<GoodsBrandDTO> findIndexBrands() {
        return redisClient.get(GoodsKey.INDEX_BRANDS, null, () -> {
            GoodsBrandDTO queryParam = new GoodsBrandDTO();
            queryParam.setIsIndexShow(1);
            int brandCount = goodsBrandMapper.getStoreBrandCount(queryParam);
            if (brandCount == 0) {
                return null;
            }
            int firstIndex = 0;
            int showCount = Integer.parseInt(globalConfig.get(GlobalConfig.INDEX_BRAND_SHOW_COUNT));
            if (brandCount > showCount) {
                firstIndex = RandomUtils.nextInt(0, brandCount - showCount + 1);
            }
            queryParam.setFirstIndex(firstIndex);
            queryParam.setShowCount(showCount);
            return goodsBrandMapper.findStoreBrands(queryParam);
        });
    }
}
