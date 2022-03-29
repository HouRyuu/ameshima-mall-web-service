package com.tmall.order.web;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.PublicResult;
import com.tmall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class OrderResource {

    @Autowired
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

}
