package com.tmall.user.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.user.entity.po.UserAlipayPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface UserAlipayMapper extends BaseMapper<UserAlipayPO> {

    int saveOrUpdate(UserAlipayPO userAlipay);
}
