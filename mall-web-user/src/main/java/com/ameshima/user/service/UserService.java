package com.ameshima.user.service;

import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PublicResult;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface UserService {

    int createUser(LoginUser user);

    PublicResult<?> update(LoginUser user);

}
