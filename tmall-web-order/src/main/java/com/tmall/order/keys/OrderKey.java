package com.tmall.order.keys;

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
public class OrderKey extends BaseKeyPrefix {

    public static final KeyPrefix ORDER_MQ = new OrderKey("order_mq", 1, TimeUnit.DAYS);
    public static final KeyPrefix ORDER_NO = new OrderKey("order_no", 1L, TimeUnit.DAYS);
    private OrderKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
