package com.tmall.store.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.dto.PublicResult;
import com.tmall.store.service.StoreBannerService;
import com.tmall.store.service.StoreBrandService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
public class StoreController {

    @Resource
    private StoreBannerService storeBannerService;
    @Resource
    private StoreBrandService storeBrandService;

    @GetMapping("/index/banner")
    public PublicResult<?>  indexBanner() {
        return PublicResult.success(storeBannerService.findIndexBanner());
    }

    @GetMapping("/index/brand")
    public PublicResult<?>  indexBrands() {
        return PublicResult.success(storeBrandService.findIndexBrands());
    }

}
