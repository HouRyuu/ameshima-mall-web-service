package com.tmall.common.constants;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum UserErrResultEnum implements IErrResult {
    LOGIN_FAIL(200, "携帯番号またはパスワードが違います"),
    NOT_LOGIN(201, "お先にログインに行きましょう"),
    INVALID_LOGIN(202, "長い間操作してなくて、安全の為に登録状態もう無効になりました。改めてご登録お願いいたします"),
    CAPTCHA_ERR(203,"キャプチャが違います"),
    REG_FAIL(204, "新規登録が失敗してしましました"),
    REG_ACCOUNT_EXISTS(205, "携帯番号もう登録されました"),
    ACCOUNT_NOT_EXISTS(206, "携帯番号がありません");

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
