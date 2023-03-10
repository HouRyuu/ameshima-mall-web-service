package com.tmall.common.constants;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public final class TmallConstant {

    public static final String JSON_STR = "json";
    public static final String UTF8_STR = "UTF-8";
    public static final String PROJECT_NAME = "tmall-web";

    public static final short ACCOUNT_TYPE_DEFAULT = 0;
    public static final short ACCOUNT_TYPE_ALIPAY = 1;

    public static final String PARAM_ERR_MSG = "パラメーター異常";

    public static final String REG_MOBILE = "^1[3-8]\\d{9}$";

    public static final short REGION_LEVEL_PROVINCE = 2;

    public static final short YES = 1;
    public static final short NO = 0;

    public static final String TOKEN = "token";

    public static final String UNDERLINE = "_";

    public enum OrderStateEnum {
        DONE((short) 0), NO_PAY((short) 1), NO_DISPATCH((short) 2), DISPATCH((short) 3), NO_COMMENT((short) 4), CANCEL((short) 5);

        private final short state;

        OrderStateEnum(short state) {
            this.state = state;
        }

        public short getState() {
            return state;
        }
    }

    public enum LogisticsStateEnum {
        NO_DISPATCH((short) 0), DISPATCH((short) 1), DONE((short) 2);

        private final short state;

        LogisticsStateEnum(short state) {
            this.state = state;
        }

        public short getState() {
            return state;
        }
    }

    public enum PayStateEnum {
        FAIL((short) -1), DEFAULT((short) 0), DONE((short) 1);

        private final short state;

        PayStateEnum(short state) {
            this.state = state;
        }

        public short getState() {
            return state;
        }
    }

}
