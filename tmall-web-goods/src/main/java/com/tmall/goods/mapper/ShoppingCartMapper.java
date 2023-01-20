package com.tmall.goods.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tmall.common.BaseMapper;
import com.tmall.remote.goods.dto.CartGoodsDTO;
import com.tmall.goods.entity.dto.ShoppingCartDTO;
import com.tmall.goods.entity.po.ShoppingCartPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCartPO> {

    int getCartCount(@Param("accountId") int accountId);

    int add(ShoppingCartDTO param);

    List<CartGoodsDTO> findGoods(@Param("accountId") int accountId);

    int remove(@Param("ids") Set<Integer> ids, @Param("accountId") int accountId);

    void updateAmount(@Param("cartId") int cartId, @Param("amount") int amount, @Param("accountId") int accountId);

    void removeFailCart(@Param("accountId") int accountId);
}
