package com.tmall.common.redis;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings({ "unchecked" })
@Component
public class RedisClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 存储至redis
     * 
     * @param prefix key前缀（可包含过期时间）
     * @param key key
     * @param value 值【普通对象或集合容器反省对象必须实现序列化接口Serializable】
     * @param <T> 泛型
     * @return 存储是否成功
     */
    public <T> boolean set(KeyPrefix prefix, Object key, T value) {
        try {
            if (prefix.timeout() <= 0) {
                redisTemplate.opsForValue().set(realKey(prefix, key), value);
                return true;
            }
            redisTemplate.opsForValue().set(realKey(prefix, key), value, prefix.timeout(), prefix.timeunit());
            return true;
        } catch (Exception e) {
            LOGGER.error("set error", e);
            return false;
        }
    }

    /**
     * 根据key获取redis缓存
     * 
     * @param prefix key前缀
     * @param key key
     * @param <T> 泛型
     * @return 值
     */
    public <T> T get(KeyPrefix prefix, Object key) {
        try {
            ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
            return valueOperations.get(realKey(prefix, key));
        } catch (Exception e) {
            LOGGER.error("get error", e);
            return null;
        }
    }

    public <T> T get(KeyPrefix prefix, String key, Callable<T> callable) {
        try {
            ValueOperations<String, T> valueOperations = redisTemplate.opsForValue();
            T result = valueOperations.get(realKey(prefix, key));
            if (result != null) {
                return result;
            }
            result = callable.call();
            if (result != null) {
                set(prefix, key, result);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("get error", e);
            return null;
        }
    }

    /**
     * 根据key删除redis缓存
     *
     * @param prefix key前缀
     * @param key key
     * @return 是否成功
     */
    public boolean removeKey(KeyPrefix prefix, Object key) {
        try {
            redisTemplate.delete(realKey(prefix, key));
            return true;
        } catch (Exception e) {
            LOGGER.error("removeKey error", e);
            return false;
        }
    }

    /**
     * 增加或减少指定数值
     * 
     * @param prefix key前缀
     * @param key key
     * @param number 数值
     */
    public Long incrOrDecr(KeyPrefix prefix, Object key, long number) {
        if (number == 0) {
            return null;
        }
        return new RedisAtomicLong(realKey(prefix, key), redisTemplate.getConnectionFactory()).decrementAndGet();
    }

    /**
     * 生成最终缓存的key
     * 
     * @param prefix 前缀
     * @param key key
     * @return 最终缓存的key
     */
    private String realKey(KeyPrefix prefix, Object key) {
        if (key == null) {
            return prefix.prefix();
        }
        return prefix.prefix() + ":" + key.toString();
    }
}
