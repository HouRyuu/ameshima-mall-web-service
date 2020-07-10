package com.tmall.store.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.dto.PublicResult;
import com.tmall.store.service.StoreBannerService;
import com.tmall.store.service.StoreBrandService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
public class StoreController {

    @Autowired
    private StoreBannerService storeBannerService;
    @Autowired
    private StoreBrandService storeBrandService;

    @GetMapping("/index/banner")
    public PublicResult indexBanner() {
        return PublicResult.success(storeBannerService.findIndexBanner());
    }

    @GetMapping("/index/brand")
    public PublicResult indexBrands() {
        return PublicResult.success(storeBrandService.findIndexBrands());
    }

}
