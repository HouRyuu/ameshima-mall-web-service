package com.tmall.goods.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.goods.entity.po.GoodsSkuPO;
import com.tmall.remote.goods.dto.CartGoodsDTO;

public interface GoodsSkuMapper extends BaseMapper<GoodsSkuPO> {

    int quantityDown(CartGoodsDTO sku);

}
