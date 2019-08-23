package com.tmall.goods.keys;

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
public class GoodsKey extends BaseKeyPrefix {

    public static final KeyPrefix INDEX_BRANDS = new GoodsKey("index_brands", 2L, TimeUnit.HOURS);
    public static final KeyPrefix INDEX_PROMOTE_PLATE = new GoodsKey("index_promote_plate", 2L, TimeUnit.HOURS);
    public static final KeyPrefix STORE_INDEX_GOODS = new GoodsKey("store_index_goods", 2L, TimeUnit.HOURS);
    public static final KeyPrefix GOODS_ATTRS = new GoodsKey("goods_attrs", 1L, TimeUnit.DAYS);
    public static final KeyPrefix GOODS_IMGS = new GoodsKey("goods_imgs", 1L, TimeUnit.DAYS);
    public static final KeyPrefix GOODS_PARAMS = new GoodsKey("goods_params", 1L, TimeUnit.DAYS);

    private GoodsKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }
}
