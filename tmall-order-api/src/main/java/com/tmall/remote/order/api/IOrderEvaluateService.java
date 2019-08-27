package com.tmall.remote.order.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@FeignClient(value = "tmall-order",path = "/order/evaluate")
public interface IOrderEvaluateService {

    @GetMapping("/{goodsId}/count")
    int count(@PathVariable("goodsId") int goodsId);

}
