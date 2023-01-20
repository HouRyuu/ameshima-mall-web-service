package com.tmall.common.constants;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tmall.common.mapper.GlobalConfigMapper;
import com.tmall.common.po.GlobalConfigPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Component
public class GlobalConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalConfig.class);
    public static final char CONNECTOR = '_';
    public static final String KEY_LIMIT_CAPTCHA = "limit_captcha";

    public static final int GOODS_IMG_TYPE_COVER = 13;
    public static final int GOODS_IMG_TYPE_DETAIL = 14;

    public static final String INDEX_BANNER_SHOW_COUNT = "index_bannerShowCount";
    public static final String INDEX_BRAND_SHOW_COUNT = "index_brandShowCount";
    public static final String INDEX_GUESS_LIKE_COUNT = "index_guessLikeCount";

    public static final String SEARCH_PAGE_SIZE = "search_pageSize";

    public static final String CART_PAGE_SIZE = "cart_pageSize";

    public static final String ADDRESS_MAX_COUNT = "address_maxCount";

    public static final String USER_DEFAULT_AVATAR = "user_defaultAvatar";


    @Resource
    private GlobalConfigMapper globalConfigMapper;

    private final Cache<String, String> CONFIG_CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).build();

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
