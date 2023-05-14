package com.ameshima.order.web;

import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.order.entity.vo.OrderEvaluateVO;
import com.ameshima.order.service.OrderEvaluateService;
import org.springframework.web.bind.annotation.*;

import com.ameshima.remote.order.api.IOrderEvaluateService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
