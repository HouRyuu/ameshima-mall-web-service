package com.ameshima.store.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.ameshima.common.constants.GlobalConfig;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.store.entity.dto.StoreBannerDTO;
import com.ameshima.store.keys.StoreKey;
import com.ameshima.store.mapper.StoreBannerMapper;
import com.ameshima.store.service.StoreBannerService;

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
public class StoreBannerServiceImpl implements StoreBannerService {

    @Resource
    private RedisClient redisClient;
    @Resource
    private StoreBannerMapper storeBannerMapper;
    @Resource
    private GlobalConfig globalConfig;

    @Override
    public List<StoreBannerDTO> findIndexBanner() {
        return redisClient.get(StoreKey.INDEX_BANNER, null, () -> {
            int bannerCount = storeBannerMapper.getBannerCount(1);
            if (bannerCount == 0) {
                return null;
            }
            int firstIndex = 0;
            int showCount = Integer.parseInt(globalConfig.get(GlobalConfig.INDEX_BANNER_SHOW_COUNT));
            if (bannerCount > showCount) {
                firstIndex = RandomUtils.nextInt(0, bannerCount - showCount + 1);
            }
            StoreBannerDTO queryParam = new StoreBannerDTO();
            queryParam.setIsIndexShow(1);
            queryParam.setFirstIndex(firstIndex);
            queryParam.setShowCount(showCount);
            return storeBannerMapper.findBanners(queryParam);
        });
    }
}
