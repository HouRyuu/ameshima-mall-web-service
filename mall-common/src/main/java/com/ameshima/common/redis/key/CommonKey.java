package com.ameshima.common.redis.key;

import java.util.concurrent.TimeUnit;

import com.ameshima.common.constants.MallConstant;
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
public class CommonKey extends BaseKeyPrefix {

    public static final KeyPrefix TOKEN = new CommonKey(MallConstant.TOKEN, 30, TimeUnit.MINUTES);

    private CommonKey(String prefix, long timeout, TimeUnit timeUnit) {
        super(prefix, timeout, timeUnit);
    }

}
