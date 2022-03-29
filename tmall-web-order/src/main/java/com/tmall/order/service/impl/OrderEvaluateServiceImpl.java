package com.tmall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.order.entity.po.OrderEvaluatePO;
import com.tmall.order.mapper.OrderEvaluateMapper;
import com.tmall.order.service.OrderEvaluateService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class OrderEvaluateServiceImpl implements OrderEvaluateService {

    @Autowired
    private OrderEvaluateMapper orderEvaluateMapper;

    @Override
    public int goodsEvaluateCount(int goodsId) {
        OrderEvaluatePO record = new OrderEvaluatePO();
        record.setGoodsId(goodsId);
        return orderEvaluateMapper.selectCount(record);
    }
}
