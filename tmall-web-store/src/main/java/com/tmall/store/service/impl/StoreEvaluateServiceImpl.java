package com.tmall.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.store.entity.dto.StoreEvaluateDTO;
import com.tmall.store.mapper.StoreEvaluateMapper;
import com.tmall.store.service.StoreEvaluateService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class StoreEvaluateServiceImpl implements StoreEvaluateService {

    @Autowired
    private StoreEvaluateMapper storeEvaluateMapper;

    @Override
    public StoreEvaluateDTO getStoreEvaluate(int storeId) {
        return storeEvaluateMapper.getStoreEvaluate(storeId);
    }
}
