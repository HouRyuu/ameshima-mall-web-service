package com.tmall.common.constants;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public enum UserErrResultEnum implements IErrResult {
    LOGIN_FAIL(200, "メールアドレスまたはパスワードが違います"),
    NOT_LOGIN(201, "お先にログインに行きましょう"),
    INVALID_LOGIN(202, "長い間操作してなくて、安全の為に登録状態もう無効になりました。改めてご登録お願いいたします"),
    CAPTCHA_ERR(203,"キャプチャが違います"),
    REG_FAIL(204, "新規登録が失敗してしましました"),
    REG_ACCOUNT_EXISTS(205, "メールアドレスもう登録されました"),
    ACCOUNT_NOT_EXISTS(206, "メールアドレスがありません");

    private final int errCode;
    private final String errMsg;

    UserErrResultEnum(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int errCode() {
        return errCode;
    }

    @Override
    public String errMsg() {
        return errMsg;
    }
}
