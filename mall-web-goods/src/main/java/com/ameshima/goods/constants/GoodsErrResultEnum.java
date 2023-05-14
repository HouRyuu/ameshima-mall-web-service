package com.ameshima.goods.constants;

import com.ameshima.common.constants.IErrResult;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public enum GoodsErrResultEnum implements IErrResult {
    ADD_CART_FAIL(300, "在庫を超えましたよ"),
    BUY_CACHE_NOT_EXISTS(301, "長い間操作していないので、改めて購入してください"),
    AMOUNT_OVER(302, "商品が大人気なので在庫は足りなくなってしまいました😹"),
    SKU_REPEAT(303, "同じSKUが存在しているので、改めてご設定ください"),
    NO_SKU(304, "デフォルトSKUが設定されてないです"),
    NO_PAY_ORDER_EXISTS(305, "支払っていない注文があるので、今は棚卸できません");

    private final int errCode;
    private final String errMsg;

    GoodsErrResultEnum(int errCode, String errMsg) {
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
