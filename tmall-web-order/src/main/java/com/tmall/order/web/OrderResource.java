package com.tmall.order.web;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.order.entity.dto.OrderConditionDTO;
import com.tmall.order.entity.vo.OrderDetailVO;
import com.tmall.order.service.OrderService;
import com.tmall.remote.order.api.IOrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class OrderResource implements IOrderService {

    @Resource
    private OrderService orderService;

    @LoginRequire
    @PostMapping("/create/{cityCode}")
    public PublicResult<?> orderToQueue(@PathVariable String cityCode, @RequestBody Map<String, String> addressMap) {
        return orderService.orderTuQueue(cityCode, addressMap.get("address"));
    }

    @LoginRequire
    @GetMapping("/created/{parentOrderNo}")
    public PublicResult<Integer> orderQueueState(@PathVariable String parentOrderNo) {
        return orderService.getOrderQueueState(parentOrderNo);
    }

    @LoginRequire
    @GetMapping("/{parentOrderNo}/goods/{orderState}")
    public PublicResult<List<OrderDetailVO>> findOrderGoodsList(@PathVariable String parentOrderNo, @PathVariable short orderState) {
        return orderService.findOrderGoodsList(parentOrderNo, orderState);
    }

    @LoginRequire
    @PostMapping("/page")
    public PublicResult<PageResult<OrderDetailVO>> findOrderGoodsList(@RequestBody OrderConditionDTO condition) {
        condition.setAccountId(LoginInfo.get().getAccountId());
        return orderService.orderPage(condition);
    }

    @LoginRequire
    @PutMapping("/{parentOrderNo}/pay")
    public PublicResult<?> payOrder(@PathVariable String parentOrderNo) {
        return orderService.payOrder(parentOrderNo);
    }

    @LoginRequire
    @PutMapping("/{orderNo}/receive/confirm")
    public PublicResult<?> receiveConfirm(@PathVariable String orderNo) {
        return orderService.receiveConfirm(orderNo);
    }

    @Override
    @GetMapping("/{goodsId}/{status}/exists")
    public boolean orderGoodsExists(@PathVariable("goodsId") int goodsId, @PathVariable("status") short status) {
        return orderService.orderGoodsExists(goodsId, status);
    }

}
