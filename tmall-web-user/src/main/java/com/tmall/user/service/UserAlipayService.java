package com.tmall.user.service;

import com.tmall.common.dto.LoginUser;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface UserAlipayService {

    LoginUser login(String authCode);

}
