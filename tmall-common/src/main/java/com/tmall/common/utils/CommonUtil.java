package com.tmall.common.utils;

import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pagehelper.PageInfo;
import com.tmall.common.dto.PageResult;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
