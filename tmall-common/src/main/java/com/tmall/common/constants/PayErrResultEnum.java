package com.tmall.common.constants;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public enum PayErrResultEnum implements IErrResult {
//    NO_PAY(601, "合計０円なので、会計の必要がありません"),
    DONE(600, "ありがとうございます。会計完了しました。"),
    EXPIRY(601, "支払う期限をすぎましたので、しばらくお待ちになってから、改めてお支払いください"),
    DUPLICATE(602, "短時間内重複に支払わないように、しばらくお待ちになってから、改めてお支払いください");

    private int errCode;
    private String errMsg;

    PayErrResultEnum(int errCode, String errMsg) {
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
