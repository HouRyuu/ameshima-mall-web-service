package com.tmall.order.utils;

import com.tmall.common.redis.RedisClient;
import com.tmall.order.keys.OrderKey;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Calendar;

@Component
public class OrderUtils {
    private static final long ORDER_NO_START = 100000000000L;

    @Resource
    private RedisClient redisClient;

    public String generateOrderNo() {
        String today = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(Calendar.getInstance());
        String now = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss");
        Long startNo = redisClient.get(OrderKey.ORDER_NO, today), firstNo = ORDER_NO_START + RandomUtils.nextLong(NumberUtils.LONG_ZERO, ORDER_NO_START - 1L);
        if (startNo == null && redisClient.setIfAbsent(OrderKey.ORDER_NO, today, firstNo)) {
            startNo = firstNo;
        } else {
            startNo = redisClient.incrOrDecr(OrderKey.ORDER_NO, today, NumberUtils.LONG_ONE);
        }
        return now + startNo;
    }

}
