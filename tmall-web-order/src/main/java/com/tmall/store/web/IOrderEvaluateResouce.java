package com.tmall.store.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.remote.order.api.IOrderEvaluateService;
import com.tmall.store.service.OrderEvaluateService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/order/evaluate")
public class IOrderEvaluateResouce implements IOrderEvaluateService {

    @Autowired
    private OrderEvaluateService orderEvaluateService;

    @Override
    @GetMapping("/{goodsId}/count")
    public int count(@PathVariable int goodsId) {
        return orderEvaluateService.goodsEvaluateCount(goodsId);
    }
}
