package com.tmall.common.constants;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tmall.common.mapper.GlobalConfigMapper;
import com.tmall.common.po.GlobalConfigPO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
public class GlobalConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfig.class);
    private static final char CONNECTOR = '_';
    public static final String KEY_LIMIT_CAPTCHA = "limit_captcha";

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    private Cache<String, String> CONFIG_CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

    @PostConstruct
    public void init() {
        List<GlobalConfigPO> configList = globalConfigMapper.select(new GlobalConfigPO());
        for (GlobalConfigPO config : configList) {
            CONFIG_CACHE.put(config.getCategory() + CONNECTOR + config.getName(), config.getValue());
        }
    }

    public String get(String configName) {
        try {
            return CONFIG_CACHE.get(configName,
                    () -> globalConfigMapper.selectOne(new GlobalConfigPO(configName)).getValue());
        } catch (ExecutionException e) {
            LOGGER.error("Global config get fail. configName=>{}", configName, e);
        }
        return null;
    }

}