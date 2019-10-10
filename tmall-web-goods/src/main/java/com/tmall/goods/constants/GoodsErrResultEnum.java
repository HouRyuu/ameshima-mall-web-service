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
    ADD_CART_FAIL(300, "超过库存数量了哦"), DEL_CART_FAIL(301, "删除失败，请稍后再试");

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
