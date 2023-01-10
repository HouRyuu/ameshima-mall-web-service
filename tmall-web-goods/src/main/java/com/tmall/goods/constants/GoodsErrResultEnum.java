package com.tmall.goods.constants;

import com.tmall.common.constants.IErrResult;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public enum GoodsErrResultEnum implements IErrResult {
    ADD_CART_FAIL(300, "在庫を超えましたよ"),
    BUY_CACHE_NOT_EXISTS(301, "長い間操作していないので、改めて購入してください"),
    AMOUNT_OVER(302, "商品が大人気なので在庫は足りなくなってしまいました😹"),
    SKU_REPEAT(303, "同じSKUが存在しているので、改めてご設定ください"),
    NO_SKU(304, "デフォルトSKUが設定されてないです");

    private int errCode;
    private String errMsg;

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
