package com.ameshima.goods.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.goods.entity.po.GoodsSkuPO;
import com.ameshima.remote.goods.dto.CartGoodsDTO;

public interface GoodsSkuMapper extends BaseMapper<GoodsSkuPO> {

    int quantityDown(CartGoodsDTO sku);

}
