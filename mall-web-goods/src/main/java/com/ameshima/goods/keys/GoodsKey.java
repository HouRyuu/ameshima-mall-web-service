package com.ameshima.goods.keys;

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
public class GoodsKey extends BaseKeyPrefix {

    public static final KeyPrefix INDEX_PROMOTE_PLATE = new GoodsKey("index_promote_plate", 2L, TimeUnit.HOURS);
    public static final KeyPrefix STORE_INDEX_GOODS = new GoodsKey("store_index_goods", 2L, TimeUnit.HOURS);
    public static final KeyPrefix GOODS_ATTRS = new GoodsKey("goods_attrs", 1L, TimeUnit.DAYS);
    public static final KeyPrefix GOODS_IMGS = new GoodsKey("goods_imgs", 1L, TimeUnit.DAYS);
    public static final KeyPrefix GOODS_PARAMS = new GoodsKey("goods_params", 1L, TimeUnit.DAYS);
    public static final KeyPrefix USER_BUY_SKUS = new GoodsKey("user_buy_skus", 30L, TimeUnit.MINUTES);

    private GoodsKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
