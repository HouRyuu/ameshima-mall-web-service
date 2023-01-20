package com.tmall.user.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.common.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;

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
