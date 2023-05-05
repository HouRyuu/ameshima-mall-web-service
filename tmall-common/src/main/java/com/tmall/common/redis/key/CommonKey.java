package com.tmall.common.redis.key;

import java.util.concurrent.TimeUnit;

import com.tmall.common.constants.MallConstant;
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
public class CommonKey extends BaseKeyPrefix {

    public static final KeyPrefix TOKEN = new CommonKey(MallConstant.TOKEN, 30, TimeUnit.MINUTES);

    private CommonKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }

}
