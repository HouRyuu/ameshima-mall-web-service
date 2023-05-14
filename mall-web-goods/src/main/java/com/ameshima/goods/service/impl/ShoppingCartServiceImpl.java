package com.ameshima.goods.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ameshima.goods.constants.GoodsErrResultEnum;
import com.ameshima.goods.entity.po.ShoppingCartPO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.remote.goods.dto.CartGoodsDTO;
import com.ameshima.goods.entity.dto.ShoppingCartDTO;
import com.ameshima.remote.goods.vo.ShopCartVO;
import com.ameshima.goods.mapper.ShoppingCartMapper;
import com.ameshima.goods.service.ShoppingCartService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    public PublicResult<?> add(ShoppingCartDTO param) {
        Assert.isTrue(param != null && param.getSkuId() != 0 && StringUtils.isNotBlank(param.getAttrsJson())
                && param.getAmount() != 0, MallConstant.PARAM_ERR_MSG);
        ShoppingCartPO cartPO = new ShoppingCartPO();
        cartPO.setAccountId(param.getAccountId());
        cartPO.setSkuId(param.getSkuId());
        cartPO.setIsDelete(MallConstant.NO);
        ShoppingCartPO cart = shoppingCartMapper.selectOne(cartPO);
        cartPO.setAttrsJson(param.getAttrsJson());
        int updateRecode;
        if (cart != null) {
            cartPO.setId(cart.getId());
            cartPO.setAmount(param.getAmount() + cart.getAmount());
            updateRecode = shoppingCartMapper.updateByPrimaryKey(cartPO);
        } else {
            cartPO.setAmount(param.getAmount());
            updateRecode = shoppingCartMapper.insertSelective(cartPO);
        }
        if (updateRecode > 0) {
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
    public PublicResult<?> remove(Set<Integer> ids, int accountId) {
        Assert.isTrue(!CollectionUtils.isEmpty(ids), MallConstant.PARAM_ERR_MSG);
        try {
            shoppingCartMapper.remove(ids, accountId);
            LOGGER.info("ショッピングセットを削除した。accountId->{} cartIds->{}", accountId, ids);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Remove shopping-cart Fail. Param => {ids:{}, accountId:{}}", StringUtils.join(ids), accountId,
                    e);
            return PublicResult.error();
        }
    }

    @Override
    public PublicResult<?> updateAmount(int cartId, int amount, int accountId) {
        Assert.isTrue(cartId > 0 && amount > 0, MallConstant.PARAM_ERR_MSG);
        try {
            shoppingCartMapper.updateAmount(cartId, amount, accountId);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Update shopping-cart's amount Fail. Param => {cartId:{}, amount:{}, accountId:{}}", cartId,
                    amount, accountId, e);
            return PublicResult.error();
        }
    }

    @Override
    public PublicResult<?> removeFailCart(int accountId) {
        try {
            shoppingCartMapper.removeFailCart(accountId);
            return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error("Remove fail cart's goods was Fail. Param => {accountId:{}}", accountId, e);
            return PublicResult.error();
        }
    }
}
