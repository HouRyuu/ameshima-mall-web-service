package com.ameshima.order.web;

import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.order.entity.dto.OrderConditionDTO;
import com.ameshima.order.entity.vo.OrderDetailVO;
import com.ameshima.order.service.OrderService;
import com.ameshima.remote.order.api.IOrderService;
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
    @PostMapping("/store/page")
    public PublicResult<PageResult<OrderDetailVO>> storeOrderGoodsList(@RequestBody OrderConditionDTO condition) {
        condition.setStoreId(LoginInfo.get().getStoreId());
        return orderService.orderPage(condition);
    }

    @LoginRequire
    @GetMapping("/{parentOrderNo}/{orderNo}/paypay/code")
    public PublicResult<?> createPayPayCode(@PathVariable String parentOrderNo, @PathVariable String orderNo) {
        return orderService.createPayPayCode(parentOrderNo, orderNo);
    }

    @LoginRequire
    @PutMapping("/{parentOrderNo}/{orderNo}/paypay/status")
    public PublicResult<?> getPayPayStatus(@PathVariable String parentOrderNo, @PathVariable String orderNo) {
        return orderService.getPayPayStatus(parentOrderNo, orderNo);
    }

//    @LoginRequire
//    @PutMapping("/{parentOrderNo}/{orderNo}/pay")
//    public PublicResult<?> payOrder(@PathVariable String parentOrderNo, @PathVariable String orderNo) {
//        return orderService.payOrder(parentOrderNo, orderNo);
//    }

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
