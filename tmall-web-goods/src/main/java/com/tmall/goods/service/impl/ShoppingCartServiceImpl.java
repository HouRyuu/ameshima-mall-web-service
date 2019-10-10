package com.tmall.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.AjaxResult;
import com.tmall.common.dto.LoginInfo;
import com.tmall.goods.constants.GoodsErrResultEnum;
import com.tmall.goods.entity.dto.CartGoodsDTO;
import com.tmall.goods.entity.dto.ShoppingCartDTO;
import com.tmall.goods.entity.vo.ShopCartVO;
import com.tmall.goods.mapper.ShoppingCartMapper;
import com.tmall.goods.service.ShoppingCartService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public AjaxResult add(ShoppingCartDTO param) {
        if (param != null) {
            param.setAccountId(LoginInfo.get().getAccountId());
        }
        Assert.isTrue(param != null && param.getSkuId() != 0 && StringUtils.isNotBlank(param.getAttrsJson())
                && param.getAmount() != 0, TmallConstant.PARAM_ERR_MSG);
        if (shoppingCartMapper.add(param) > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error(GoodsErrResultEnum.ADD_CART_FAIL);
    }

    @Override
    public int getCartCount() {
        return shoppingCartMapper.getCartCount(LoginInfo.get().getAccountId());
    }

    @Override
    public Collection<ShopCartVO> findGoods() {
        List<CartGoodsDTO> goodsList = shoppingCartMapper.findGoods(LoginInfo.get().getAccountId());
        Map<ShopCartVO, ShopCartVO> cartMap = Maps.newLinkedHashMap();
        ShopCartVO shopCart, temp;
        for (CartGoodsDTO goods : goodsList) {
            temp = new ShopCartVO(goods.getStoreId(), goods.getStoreName(), goods.getState());
            shopCart = cartMap.get(temp);
            if (shopCart == null) {
                shopCart = temp;
                shopCart.setGoodsList(Lists.newArrayList());
                cartMap.put(temp, shopCart);
            }
            shopCart.getGoodsList().add(goods);
        }
        return cartMap.values();
    }

    @Override
    public AjaxResult remove(Set<Integer> ids) {
        Assert.isTrue(!CollectionUtils.isEmpty(ids), TmallConstant.PARAM_ERR_MSG);
        int accountId = LoginInfo.get().getAccountId();
        try {
            shoppingCartMapper.remove(ids, accountId);
            return AjaxResult.success();
        } catch (Exception e) {
            LOGGER.error("Remove shopping-cart Fail. Param => {ids:{}, accountId:{}}", StringUtils.join(ids), accountId,
                    e);
            return AjaxResult.error(GoodsErrResultEnum.DEL_CART_FAIL);
        }
    }
}
