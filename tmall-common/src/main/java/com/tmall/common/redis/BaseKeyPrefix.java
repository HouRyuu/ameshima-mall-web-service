package com.tmall.common.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.tmall.common.constants.TmallConstant;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
            return TmallConstant.PROJECT_NAME + "_" + this.getClass().getSimpleName() + ":" + this.prefix;
        }
        return TmallConstant.PROJECT_NAME + "_" + this.getClass().getSimpleName();
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
