package com.tmall.goods.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.tmall.common.BaseMapper;
import com.tmall.remote.goods.dto.CartGoodsDTO;
import com.tmall.goods.entity.dto.ShoppingCartDTO;
import com.tmall.goods.entity.po.ShoppingCartPO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface ShoppingCartMapper extends BaseMapper<ShoppingCartPO> {

    int getCartCount(@Param("accountId") int accountId);

    int add(ShoppingCartDTO param);

    List<CartGoodsDTO> findGoods(@Param("accountId") int accountId);

    int remove(@Param("ids") Set<Integer> ids, @Param("accountId") int accountId);

    void updateAmount(@Param("cartId") int cartId, @Param("amount") int amount, @Param("accountId") int accountId);

    void removeFailCart(@Param("accountId") int accountId);
}
