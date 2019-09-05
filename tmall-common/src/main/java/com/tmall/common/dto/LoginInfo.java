package com.tmall.common.dto;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class LoginInfo {
    private static final ThreadLocal<LoginUser> LOGIN_INFO = new ThreadLocal<>();
    private static final ThreadLocal<String> TOKEN = new ThreadLocal<>();

    public static void put(LoginUser loginUser) {
        LOGIN_INFO.set(loginUser);
    }

    public static LoginUser get() {
        return LOGIN_INFO.get();
    }

    public static void remove() {
        LOGIN_INFO.remove();
    }

    public static void putToken(String token) {
        TOKEN.set(token);
    }

    public static String getToken() {
        return TOKEN.get();
    }

    public static void removeToken() {
        TOKEN.remove();
    }
}
