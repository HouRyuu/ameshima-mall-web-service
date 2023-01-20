package com.tmall.remote.goods.api;

import com.tmall.common.dto.PublicResult;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.vo.ShopCartVO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.tmall.remote.goods.dto.GoodsDTO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
