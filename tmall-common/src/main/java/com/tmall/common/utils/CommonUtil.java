package com.tmall.common.utils;

import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pagehelper.PageInfo;
import com.tmall.common.dto.PageResult;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public final class CommonUtil {

    public static String getUuid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    public static String createCaptcha() {
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(RandomUtils.nextInt(0, 10));
        }
        return captcha.toString();
    }

    public static <E> PageResult<E> convertPage(PageInfo<E> page) {
        PageResult<E> result = new PageResult<E>();
        result.setContent(page.getList());
        result.setPageIndex(page.getPageNum());
        result.setPageSize(page.getPageSize());
        result.setTotal(page.getTotal());
        return result;
    }
}
