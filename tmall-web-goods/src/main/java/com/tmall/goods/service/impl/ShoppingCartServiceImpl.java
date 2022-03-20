package com.tmall.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tmall.common.constants.CommonErrResult;
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
import com.tmall.common.dto.PublicResult;
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
    public PublicResult<?>  add(ShoppingCartDTO param) {
        Assert.isTrue(param != null && param.getSkuId() != 0 && StringUtils.isNotBlank(param.getAttrsJson())
                && param.getAmount() != 0, TmallConstant.PARAM_ERR_MSG);
        if (shoppingCartMapper.add(param) > 0) {
            return PublicResult.success();
        }
        return PublicResult.error(GoodsErrResultEnum.ADD_CART_FAIL);
    }

    @Override
    public int getCartCount(int accountId) {
        return shoppingCartMapper.getCartCount(accountId);
    }

    @Override
    public Collection<ShopCartVO> findGoods(int accountId) {
        List<CartGoodsDTO> goodsList = shoppingCartMapper.findGoods(accountId);
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
    public PublicResult<?>  remove(Set<Integer> ids, int accountId) {
        Assert.isTrue(!CollectionUtils.isEmpty(ids), TmallConstant.PARAM_ERR_MSG);
        try {
            shoppingCartMapper.remove(ids, accountId);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Remove shopping-cart Fail. Param => {ids:{}, accountId:{}}", StringUtils.join(ids), accountId,
                    e);
            return PublicResult.error(CommonErrResult.OPERATE_FAIL);
        }
    }

    @Override
    public PublicResult<?>  updateAmount(int cartId, int amount, int accountId) {
        Assert.isTrue(cartId > 0 && amount > 0, TmallConstant.PARAM_ERR_MSG);
        try {
            shoppingCartMapper.updateAmount(cartId, amount, accountId);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Update shopping-cart's amount Fail. Param => {cartId:{}, amount:{}, accountId:{}}", cartId,
                    amount, accountId, e);
            return PublicResult.error(CommonErrResult.OPERATE_FAIL);
        }
    }

    @Override
    public PublicResult<?>  removeFailCart(int accountId) {
        try {
            shoppingCartMapper.removeFailCart(accountId);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Remove fail cart's goods was Fail. Param => {accountId:{}}", accountId, e);
            return PublicResult.error(CommonErrResult.OPERATE_FAIL);
        }
    }
}
