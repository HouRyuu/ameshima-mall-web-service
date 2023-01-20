package com.tmall.store.keys;

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
public class StoreKey extends BaseKeyPrefix {

    public static final KeyPrefix INDEX_BANNER = new StoreKey("index_banner", 2, TimeUnit.HOURS);
    public static final KeyPrefix INDEX_BRANDS = new StoreKey("index_brands", 2, TimeUnit.HOURS);

    private StoreKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
