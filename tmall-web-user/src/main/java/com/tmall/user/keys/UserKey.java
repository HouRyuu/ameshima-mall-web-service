package com.tmall.user.keys;

import java.util.concurrent.TimeUnit;

import com.tmall.common.redis.BaseKeyPrefix;
import com.tmall.common.redis.KeyPrefix;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class UserKey extends BaseKeyPrefix {

    public static final KeyPrefix CAPTCHA_REGISTER = new UserKey("captcha_register", 1, TimeUnit.MINUTES);
    public static final KeyPrefix CAPTCHA_FORGET = new UserKey("captcha_forget", 1, TimeUnit.MINUTES);

    private UserKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
