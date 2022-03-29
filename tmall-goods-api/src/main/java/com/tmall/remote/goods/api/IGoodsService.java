package com.tmall.remote.goods.api;

import com.tmall.common.dto.PublicResult;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.vo.ShopCartVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.tmall.remote.goods.dto.GoodsDTO;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@FeignClient(value = "tmall-goods", path = "/goods")
public interface IGoodsService {

    @GetMapping("/getGoods/{id}")
    GoodsDTO getGoods(@PathVariable(name = "id") Integer id);

    @RequestMapping("/goodsBySkus")
    PublicResult<List<ShopCartVO>> goodsBySkus(@RequestBody OrderAddressDTO address);

    @PostMapping("/skuOrdered/{accountId}")
    PublicResult<?> skuOrdered(@PathVariable("accountId") int accountId);

}
