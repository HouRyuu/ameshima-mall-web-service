package com.ameshima.common.constants;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public enum CommonErrResult implements IErrResult {

    OPERATE_FAIL(500, "エラーが発生してしまいました。しばらく待ってからしてみてください^_^"),
    ERR_REQUEST(250, "異常なリクエスト");

    private final int errCode;
    private final String errMsg;

    CommonErrResult(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int errCode() {
        return this.errCode;
    }

    @Override
    public String errMsg() {
        return this.errMsg;
    }
}
