package com.tmall.goods.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.dto.AjaxResult;
import com.tmall.goods.entity.dto.GuessLikeQueryDTO;
import com.tmall.goods.service.GoodsCategoryService;
import com.tmall.goods.service.GoodsPromoteService;
import com.tmall.goods.service.GoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/goods")
public class GoodsResource {

    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsPromoteService goodsPromoteService;

    @GetMapping("/hello")
    public String hello() {
        return "hello Goods";
    }

    @GetMapping("/getGoods/{id}")
    public GoodsDTO getGoods(@PathVariable Integer id) {
        GoodsDTO goods = new GoodsDTO();
        goods.setId(1);
        goods.setName("Prada 2019夏季男士短袖");
        return goods;
    }

    @GetMapping("/findSecondCategories")
    public AjaxResult findSecondCategories() {
        return AjaxResult.success(goodsCategoryService.findSecondCategories());
    }

    @GetMapping("/{pid}/findCategories")
    public AjaxResult findCategoriesByPid(@PathVariable int pid) {
        return AjaxResult.success(goodsCategoryService.findCategoriesByPid(pid));
    }

    @GetMapping("/indexPromotePlate")
    public AjaxResult indexPromotePlate() {
        return AjaxResult.success(goodsPromoteService.findPromotes());
    }

    @GetMapping("/{promoteId}/findPromoteGoods")
    public AjaxResult findPromoteGoods(@PathVariable int promoteId) {
        return AjaxResult.success(goodsService.findByPromote(promoteId));
    }

    @PostMapping("/guessLike")
    public AjaxResult guessLike(@RequestBody GuessLikeQueryDTO queryParam) {
        return AjaxResult.success(goodsService.findByCategories(queryParam));
    }

    @GetMapping("/{storeId}/findBanners")
    public AjaxResult findBanners(@PathVariable int storeId) {
        return AjaxResult.success(goodsService.findBanners(storeId));
    }
}
