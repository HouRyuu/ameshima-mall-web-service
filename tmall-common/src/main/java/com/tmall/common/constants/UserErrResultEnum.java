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
    LOGIN_FAIL(200, "手机号或密码错误"), NOT_LOGIN(201, "未登录"), INVALID_LOGIN(202, "登录已失效"), CAPTCHA_ERR(500,
            "验证码错误"), REG_FAIL(501, "注册失败"), REG_ACCOUNT_EXISTS(502, "手机号已被注册"), ACCOUNT_NOT_EXISTS(503, "手机号不存在");

    private int errCode;
    private String errMsg;

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
