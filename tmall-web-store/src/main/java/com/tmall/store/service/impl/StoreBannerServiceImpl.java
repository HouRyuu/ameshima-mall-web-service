package com.tmall.store.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.redis.RedisClient;
import com.tmall.store.entity.dto.StoreBannerDTO;
import com.tmall.store.keys.StoreKey;
import com.tmall.store.mapper.StoreBannerMapper;
import com.tmall.store.service.StoreBannerService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class StoreBannerServiceImpl implements StoreBannerService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private StoreBannerMapper storeBannerMapper;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public List<StoreBannerDTO> findIndexBanner() {
//        return redisClient.get(StoreKey.INDEX_BANNER, null, () -> {
            int bannerCount = storeBannerMapper.getBannerCount(1);
            int firstIndex = 0;
            int showCount = Integer.parseInt(globalConfig.get(GlobalConfig.BANNER_INDEX_SHOW_COUNT));
            if (bannerCount > showCount) {
                firstIndex = RandomUtils.nextInt(0, bannerCount - showCount + 1);
            }
            StoreBannerDTO queryParam = new StoreBannerDTO();
            queryParam.setIsIndexShow(1);
            queryParam.setFirstIndex(firstIndex);
            queryParam.setShowCount(showCount);
            return storeBannerMapper.findBanners(queryParam);
//        });
    }
}
