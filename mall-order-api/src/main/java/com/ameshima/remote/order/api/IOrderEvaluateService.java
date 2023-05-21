package com.ameshima.remote.order.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@FeignClient(value = "mall-order",path = "/order/evaluate")
public interface IOrderEvaluateService {

    @GetMapping("/{goodsId}/count")
    int count(@PathVariable("goodsId") int goodsId);

}
