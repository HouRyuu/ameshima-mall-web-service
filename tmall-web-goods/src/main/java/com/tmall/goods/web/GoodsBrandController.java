package com.tmall.goods.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.dto.AjaxResult;
import com.tmall.goods.service.GoodsBrandService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/goods/brand")
public class GoodsBrandController {

    @Autowired
    private GoodsBrandService goodsBrandService;

    @GetMapping("/index")
    public AjaxResult indexBrands() {
        return AjaxResult.success(goodsBrandService.findIndexBrands());
    }

}
