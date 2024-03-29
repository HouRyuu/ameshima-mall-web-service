package com.ameshima.order.keys;

import java.util.concurrent.TimeUnit;

import com.ameshima.common.redis.BaseKeyPrefix;
import com.ameshima.common.redis.KeyPrefix;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class OrderKey extends BaseKeyPrefix {

    public static final KeyPrefix ORDER_MQ = new OrderKey("order_mq", 1, TimeUnit.DAYS);
    public static final KeyPrefix ORDER_NO = new OrderKey("order_no", 1L, TimeUnit.DAYS);
    public static final KeyPrefix PAYPAY_CODE_ID = new OrderKey("paypay_code_id");
    public static final KeyPrefix PAYPAY_CODE = new OrderKey("paypay_code", 4, TimeUnit.MINUTES);

    private OrderKey(String prefix) {
        super(prefix);
    }

    private OrderKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
