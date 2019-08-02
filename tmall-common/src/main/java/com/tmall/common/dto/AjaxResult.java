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
public class AjaxResult {

    private int errCode;
    private String errMsg;
    private Object data;

    private AjaxResult() {
    }

    public AjaxResult(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public AjaxResult(Object data) {
        this.data = data;
    }

    public static AjaxResult error(int errCode, String errMsg) {
        return new AjaxResult(errCode, errMsg);
    }

    public static AjaxResult error(IErrResult errResult) {
        return new AjaxResult(errResult.errCode(), errResult.errMsg());
    }

    public static AjaxResult success() {
        return new AjaxResult();
    }

    public static AjaxResult success(Object data) {
        return new AjaxResult(data);
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

    public void setData(Object data) {
        this.data = data;
    }
}
