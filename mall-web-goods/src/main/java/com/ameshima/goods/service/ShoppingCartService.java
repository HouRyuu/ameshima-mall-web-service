package com.ameshima.goods.service;

import java.util.Collection;
import java.util.Set;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.goods.entity.dto.ShoppingCartDTO;
import com.ameshima.remote.goods.vo.ShopCartVO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface ShoppingCartService {

    PublicResult<?>  add(ShoppingCartDTO param);

    int getCartCount(int accountId);

    Collection<ShopCartVO> findGoods(int accountId);

    PublicResult<?>  remove(Set<Integer> ids, int accountId);

    PublicResult<?>  updateAmount(int cartId, int amount, int accountId);

    PublicResult<?>  removeFailCart(int accountId);
}
