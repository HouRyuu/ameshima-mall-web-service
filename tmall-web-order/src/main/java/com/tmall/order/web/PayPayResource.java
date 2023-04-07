package com.tmall.order.web;

import com.alibaba.fastjson.JSON;
import com.tmall.common.dto.PublicResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/paypay")
public class PayPayResource {

    @PostMapping("/hook/pay")
    public PublicResult<?> orderToQueue(@RequestBody Map<String, String> payParam) {
        // {"notification_type":"Transaction","store_id":"","paid_at":"2023-04-07T15:45:29+09:00","merchant_order_id":"20230407154432188410989752","pos_id":"","order_amount":"7490","merchant_id":"630945097324937216","state":"COMPLETED","order_id":"04137808670405271552"}
        System.out.println(JSON.toJSON(payParam));
        return PublicResult.success();
    }

}
