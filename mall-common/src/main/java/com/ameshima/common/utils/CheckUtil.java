package com.ameshima.common.utils;

import java.util.Collection;
import java.util.regex.Pattern;

import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.IErrResult;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.PublicResult;
import org.springframework.util.Assert;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class CheckUtil {

    public static void checkMobile(String mobile) {
        Assert.isTrue(Pattern.matches(MallConstant.REG_MOBILE, mobile), "手机号" + mobile + "格式不正确");
    }

    public static void checkEmail(String email) {
        Assert.isTrue(Pattern.matches(MallConstant.REG_EMAIL, email), email + "はメールアドレスではありません");
    }

    public static void checkStrLength(String str, int min, int max) {
        int len = str.length();
        Assert.isTrue(len >= min && len <= max, str + "长度不在范围内");
    }

    public static void checkStrLength(int min, int max, String... strs) {
        for (String str : strs) {
            int len = str.length();
            Assert.isTrue(len >= min && len <= max, str + "长度不在范围内");
        }
    }

    public static <T> PublicResult<T> isTrue(boolean expression, IErrResult errResult) {
        return expression ? null : PublicResult.error(errResult);
    }

    public static <T> PublicResult<T> isTrue(boolean expression) {
        return isTrue(expression, CommonErrResult.ERR_REQUEST);
    }

    public static <T> PublicResult<T> notNull(Object obj) {
        return isTrue(obj == null);
    }

    public static <T> PublicResult<T> notNull(Object obj, IErrResult errResult) {
        return isTrue(obj == null, errResult);
    }

    public static <T> PublicResult<T> hasLength(String text) {
        return isTrue(StringUtils.hasLength(text));
    }

    public static <T> PublicResult<T> hasLength(String text, IErrResult errResult) {
        return isTrue(StringUtils.hasLength(text), errResult);
    }

    public static <T> PublicResult<T> hasText(String text) {
        return isTrue(StringUtils.hasText(text));
    }

    public static <T> PublicResult<T> hasText(String text, IErrResult errResult) {
        return isTrue(StringUtils.hasText(text), errResult);
    }

    public static <T> PublicResult<T> notEmpty(Object[] array) {
        return isTrue(!ObjectUtils.isEmpty(array));
    }

    public static <T> PublicResult<T> notEmpty(Object[] array, IErrResult errResult) {
        return isTrue(!ObjectUtils.isEmpty(array), errResult);
    }

    public static <T> PublicResult<T> notEmpty(Collection<?> collection) {
        return isTrue(!CollectionUtils.isEmpty(collection));
    }

    public static <T> PublicResult<T> notEmpty(Collection<?> collection, IErrResult errResult) {
        return isTrue(!CollectionUtils.isEmpty(collection), errResult);
    }
}
