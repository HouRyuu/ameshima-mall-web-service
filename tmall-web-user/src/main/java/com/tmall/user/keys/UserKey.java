package com.tmall.user.keys;

import java.util.concurrent.TimeUnit;

import com.tmall.common.redis.BaseKeyPrefix;
import com.tmall.common.redis.KeyPrefix;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class UserKey extends BaseKeyPrefix {

    public static final KeyPrefix CAPTCHA_REGISTER = new UserKey("captcha_register", 10, TimeUnit.MINUTES);
    public static final KeyPrefix CAPTCHA_FORGET = new UserKey("captcha_forget", 10, TimeUnit.MINUTES);

    private UserKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
