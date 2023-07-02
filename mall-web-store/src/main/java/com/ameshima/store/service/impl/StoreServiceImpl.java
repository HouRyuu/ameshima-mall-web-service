package com.ameshima.store.service.impl;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.store.entity.po.StorePO;
import com.ameshima.store.mapper.StoreMapper;
import com.ameshima.store.service.StoreEvaluateService;
import com.ameshima.store.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class StoreServiceImpl implements StoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServiceImpl.class);

    @Resource
    private StoreMapper storeMapper;
    @Resource
    private StoreEvaluateService evaluateService;

    @Override
    public PublicResult<Integer> register(String storeName) {
        try {
            return PublicResult.success(registerWithTran(storeName));
        } catch (Exception e) {
            LOGGER.error(storeName + "-店舗登録エラー", e);
            return PublicResult.error();
        }
    }

    @Transactional
    int registerWithTran(String storeName) {
        StorePO storePO = new StorePO();
        storePO.setName(storeName);
        storeMapper.insertSelective(storePO);
        evaluateService.createEvaluate(storePO.getId());
        return storePO.getId();
    }
}
