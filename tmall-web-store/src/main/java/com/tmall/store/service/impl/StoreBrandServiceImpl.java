package com.tmall.store.service.impl;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.redis.RedisClient;
import com.tmall.store.entity.dto.StoreBrandDTO;
import com.tmall.store.keys.StoreKey;
import com.tmall.store.mapper.StoreBrandMapper;
import com.tmall.store.service.StoreBrandService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class StoreBrandServiceImpl implements StoreBrandService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private StoreBrandMapper storeBrandMapper;
    @Autowired
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
