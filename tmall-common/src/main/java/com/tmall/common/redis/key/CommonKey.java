package com.tmall.common.redis.key;

import java.util.concurrent.TimeUnit;

import com.tmall.common.constants.TmallConstant;
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
public class CommonKey extends BaseKeyPrefix {

    public static final KeyPrefix TOKEN = new CommonKey(TmallConstant.TOKEN, 10, TimeUnit.MINUTES);

    private CommonKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }

}
