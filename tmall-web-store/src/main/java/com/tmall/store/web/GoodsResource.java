package com.tmall.goods.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.dto.AjaxResult;
import com.tmall.goods.service.GoodsCategoryService;
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
}
