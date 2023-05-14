package com.ameshima.remote.user.api;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@FeignClient(value = "tmall-user", path = "/user")
public interface IUserService {


}
