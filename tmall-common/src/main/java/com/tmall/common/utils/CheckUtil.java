package com.tmall.common.utils;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.tmall.common.constants.TmallConstant;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class CheckUtil {

    public static void checkMobile(String mobile) {
        Assert.isTrue(Pattern.matches(TmallConstant.REG_MOBILE, mobile), "手机号" + mobile + "格式不正确");
    }

    public static void checkStrLength(String str, int min, int max) {
        int len = str.length();
        Assert.isTrue(len >= min && len <= max, str + "长度不在范围内");
    }

}
