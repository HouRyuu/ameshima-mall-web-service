package com.tmall.goods.service;

import java.util.Collection;
import java.util.Set;

import com.tmall.common.dto.AjaxResult;
import com.tmall.goods.entity.dto.ShoppingCartDTO;
import com.tmall.goods.entity.vo.ShopCartVO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface ShoppingCartService {

    AjaxResult add(ShoppingCartDTO param);

    int getCartCount();

    Collection<ShopCartVO> findGoods();

    AjaxResult remove(Set<Integer> ids);

}
