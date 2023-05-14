package com.ameshima.common.constants;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public enum UserErrResultEnum implements IErrResult {
    LOGIN_FAIL(400, "メールアドレスまたはパスワードが違います"),
    NOT_LOGIN(401, "お先にログインに行きましょう"),
    INVALID_LOGIN(402, "長い間操作してなくて、安全の為に登録状態もう無効になりました。改めてご登録お願いいたします"),
    CAPTCHA_ERR(403,"キャプチャが違います"),
    ACCOUNT_NOT_EXISTS(404, "メールアドレスがありません"),
    REG_ACCOUNT_EXISTS(405, "メールアドレスもう登録されました"),
    REG_FAIL(406, "新規登録が失敗してしましました");

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
