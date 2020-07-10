package com.tmall.common.dto;

import com.tmall.common.constants.IErrResult;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@SuppressWarnings("unchecked")
public class PublicResult<T> {

    private int errCode;
    private String errMsg;
    private T data;

    private PublicResult() {
    }

    public PublicResult(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public PublicResult(T data) {
        this.data = data;
    }

    public static <T> PublicResult<T> error(int errCode, String errMsg) {
        return new PublicResult(errCode, errMsg);
    }

    public static <T> PublicResult<T> error(IErrResult errResult) {
        return new PublicResult(errResult.errCode(), errResult.errMsg());
    }

    public static PublicResult success() {
        return new PublicResult();
    }

    public static <T> PublicResult<T> success(T data) {
        return new PublicResult(data);
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
