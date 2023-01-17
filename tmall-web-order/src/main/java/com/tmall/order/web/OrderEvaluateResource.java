package com.tmall.order.web;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.order.entity.vo.OrderEvaluateVO;
import com.tmall.order.service.OrderEvaluateService;
import org.springframework.web.bind.annotation.*;

import com.tmall.remote.order.api.IOrderEvaluateService;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/evaluate")
public class OrderEvaluateResource implements IOrderEvaluateService {

    @Resource
    private OrderEvaluateService orderEvaluateService;

    @Override
    @GetMapping("/{goodsId}/count")
    public int count(@PathVariable int goodsId) {
        return orderEvaluateService.goodsEvaluateCount(goodsId);
    }

    @LoginRequire
    @PutMapping("/create")
    public PublicResult<?> createEvaluate(@RequestBody OrderEvaluateVO evaluate) {
        return orderEvaluateService.create(evaluate);
    }

    @PostMapping("/page")
    public PublicResult<PageResult<OrderEvaluateVO>> evaluatePage(@RequestBody OrderEvaluateVO evaluate) {
        return orderEvaluateService.evaluatePage(evaluate);
    }
}
