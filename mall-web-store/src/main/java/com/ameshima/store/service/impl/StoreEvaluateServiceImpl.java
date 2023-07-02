package com.ameshima.store.service.impl;

import com.ameshima.store.entity.po.StoreEvaluatePO;
import org.springframework.stereotype.Service;

import com.ameshima.store.entity.dto.StoreEvaluateDTO;
import com.ameshima.store.mapper.StoreEvaluateMapper;
import com.ameshima.store.service.StoreEvaluateService;

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
public class StoreEvaluateServiceImpl implements StoreEvaluateService {

    @Resource
    private StoreEvaluateMapper storeEvaluateMapper;

    @Override
    public StoreEvaluateDTO getStoreEvaluate(int storeId) {
        return storeEvaluateMapper.getStoreEvaluate(storeId);
    }

    @Override
    public void createEvaluate(int storeId) {
        storeEvaluateMapper.insertSelective(new StoreEvaluatePO(storeId));
    }

}
