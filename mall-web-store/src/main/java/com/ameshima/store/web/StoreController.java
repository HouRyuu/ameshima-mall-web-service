package com.ameshima.store.web;

import com.ameshima.remote.store.api.IStoreService;
import com.ameshima.store.service.StoreBannerService;
import com.ameshima.store.service.StoreBrandService;
import com.ameshima.store.service.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ameshima.common.dto.PublicResult;

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
public class StoreController implements IStoreService {

    @Resource
    private StoreBannerService storeBannerService;
    @Resource
    private StoreBrandService storeBrandService;
    @Resource
    private StoreService storeService;

    @GetMapping("/index/banner")
    public PublicResult<?> indexBanner() {
        return PublicResult.success(storeBannerService.findIndexBanner());
    }

    @GetMapping("/index/brand")
    public PublicResult<?> indexBrands() {
        return PublicResult.success(storeBrandService.findIndexBrands());
    }

    @Override
    @PutMapping("/register")
    public PublicResult<Integer> register(@RequestBody String storeName) {
        return storeService.register(storeName);
    }

}
