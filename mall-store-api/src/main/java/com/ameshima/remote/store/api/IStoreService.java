package com.ameshima.remote.store.api;

import com.ameshima.common.dto.PublicResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@FeignClient(value = "mall-store", path = "/store")
public interface IStoreService {

    @PutMapping("/register")
    PublicResult<Integer> register(@RequestBody String storeName);
}
