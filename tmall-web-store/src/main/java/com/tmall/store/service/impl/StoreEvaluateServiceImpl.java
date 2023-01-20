package com.tmall.store.service.impl;

import org.springframework.stereotype.Service;

import com.tmall.store.entity.dto.StoreEvaluateDTO;
import com.tmall.store.mapper.StoreEvaluateMapper;
import com.tmall.store.service.StoreEvaluateService;

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
}
