package com.tmall.store.keys;

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
public class StoreKey extends BaseKeyPrefix {

    public static final KeyPrefix INDEX_BANNER = new StoreKey("index_banner", 2, TimeUnit.HOURS);
    public static final KeyPrefix INDEX_BRANDS = new StoreKey("index_brands", 2, TimeUnit.HOURS);

    private StoreKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
