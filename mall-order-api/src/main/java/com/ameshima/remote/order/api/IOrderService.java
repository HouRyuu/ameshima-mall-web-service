package com.ameshima.remote.order.api;

import com.ameshima.common.constants.MallConstant;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "tmall-order", path = "/order")
public interface IOrderService {

    /**
     * 指定された状態の注文があるかどうか
     *
     * @param goodsId 商品番号
     * @param status  状態{@link MallConstant.OrderStateEnum}
     * @return ある:true ない:false
     */
    @GetMapping("/{goodsId}/{status}/exists")
    boolean orderGoodsExists(@PathVariable("goodsId") int goodsId, @PathVariable("status") short status);

}
