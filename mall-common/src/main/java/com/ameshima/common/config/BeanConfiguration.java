package com.ameshima.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;

/***
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ForkJoinPool forkJoinPool() {
        return new ForkJoinPool();
    }

    /***
     * 定时任务默认使用的Single线程池，每次只会有一个线程执行
     *
     * @return 共定时任务使用的线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskScheduler() {
        // io密集型任务线程数=cpu线程数+1
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    /**
     * key序列化默认为jdk序列化，改为StringRedisSerializer<br/>
     * value序列化方式改为json
     *
     * @param redisTemplate redisTemplate
     * @return redisTemplate
     */
    @SuppressWarnings("unchecked")
    @Bean
    public RedisTemplate configRedisTemplate(RedisTemplate redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

}
