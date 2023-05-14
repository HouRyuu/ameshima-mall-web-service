package com.ameshima.store.web;

import com.ameshima.store.service.StoreEvaluateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/evaluate")
public class StoreEvaluateController {

    @Resource
    private StoreEvaluateService storeEvaluateService;

    @GetMapping("/{storeId}/getEvaluate")
    public PublicResult<?>  getEvaluate(@PathVariable int storeId) {
        return PublicResult.success(storeEvaluateService.getStoreEvaluate(storeId));
    }

}
