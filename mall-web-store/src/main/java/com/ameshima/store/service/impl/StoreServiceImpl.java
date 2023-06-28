package com.ameshima.store.service.impl;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.store.entity.po.StorePO;
import com.ameshima.store.mapper.StoreMapper;
import com.ameshima.store.service.StoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    private StoreMapper storeMapper;

    @Override
    public PublicResult<Integer> register(String storeName) {
        StorePO storePO = new StorePO();
        storePO.setName(storeName);
        storeMapper.insertSelective(storePO);
        return PublicResult.success(storePO.getId());
    }
}
