package com.tmall.goods.utils;

import com.tmall.goods.entity.dto.GoodsGridDTO;
import com.tmall.goods.entity.po.GoodsPO;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtil {

    public static List<GoodsGridDTO> convertToGrid(List<GoodsPO> goods) {
        return goods.stream().map(po -> new GoodsGridDTO(po.getId(), po.getStoreId(), po.getName(),
                po.getSimpleDesc(), po.getPrice(), po.getImgUrl(), po.getStatus())).collect(Collectors.toList());
    }

}
