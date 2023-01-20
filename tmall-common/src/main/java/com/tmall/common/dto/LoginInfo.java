package com.tmall.common.dto;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
