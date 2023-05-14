package com.ameshima.store.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.ameshima.common.constants.GlobalConfig;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.store.entity.dto.StoreBrandDTO;
import com.ameshima.store.keys.StoreKey;
import com.ameshima.store.mapper.StoreBrandMapper;
import com.ameshima.store.service.StoreBrandService;

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
public class StoreBrandServiceImpl implements StoreBrandService {

    @Resource
    private RedisClient redisClient;
    @Resource
    private StoreBrandMapper storeBrandMapper;
    @Resource
    private GlobalConfig globalConfig;

    @Override
    public List<StoreBrandDTO> findIndexBrands() {
        return redisClient.get(StoreKey.INDEX_BRANDS, null, () -> {
            StoreBrandDTO queryParam = new StoreBrandDTO();
            queryParam.setIsIndexShow(1);
            int brandCount = storeBrandMapper.getStoreBrandCount(queryParam);
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
            return storeBrandMapper.findStoreBrands(queryParam);
        });
    }
}
