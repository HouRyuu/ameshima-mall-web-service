package com.ameshima.user.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.user.entity.po.AccountPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface AccountMapper extends BaseMapper<AccountPO> {

    LoginUser login(AccountPO account);

}
