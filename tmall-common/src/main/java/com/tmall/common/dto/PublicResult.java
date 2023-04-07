package com.tmall.common.dto;

import com.alibaba.fastjson.JSONObject;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.constants.IErrResult;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public class PublicResult<T> {

    public static final int OK_CODE = 0;

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
        return new PublicResult<>(errCode, errMsg);
    }

    public static <T> PublicResult<T> error(IErrResult errResult) {
        return new PublicResult<>(errResult.errCode(), errResult.errMsg());
    }

    public static <T> PublicResult<T> error() {
        return error(CommonErrResult.OPERATE_FAIL);
    }

    public static <T> PublicResult<T> success() {
        return new PublicResult<>();
    }

    public static <T> PublicResult<T> success(T data) {
        return new PublicResult<>(data);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
