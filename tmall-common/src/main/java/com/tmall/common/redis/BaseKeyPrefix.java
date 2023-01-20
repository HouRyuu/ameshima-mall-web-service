package com.tmall.common.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.tmall.common.constants.TmallConstant;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class BaseKeyPrefix implements KeyPrefix {

    private String prefix;
    private long timeout;
    private TimeUnit timeUnit;

    public BaseKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public BaseKeyPrefix(String prefix, long timeout) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public BaseKeyPrefix(String prefix, long timeout, TimeUnit timeUnit) {
        this.prefix = prefix;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public BaseKeyPrefix(long timeout) {
        this.timeout = timeout;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public BaseKeyPrefix(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public String prefix() {
        if (StringUtils.isNotBlank(this.prefix)) {
            return TmallConstant.PROJECT_NAME + TmallConstant.UNDERLINE + this.getClass().getSimpleName() + ":" + this.prefix;
        }
        return TmallConstant.PROJECT_NAME + TmallConstant.UNDERLINE + this.getClass().getSimpleName();
    }

    @Override
    public long timeout() {
        return this.timeout;
    }

    @Override
    public TimeUnit timeunit() {
        return this.timeUnit;
    }
}
