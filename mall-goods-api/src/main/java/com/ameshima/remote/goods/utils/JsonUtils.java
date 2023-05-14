package com.ameshima.remote.goods.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ameshima.remote.goods.dto.CartGoodsDTO;
import com.ameshima.remote.goods.vo.ShopCartVO;

import java.util.List;
import java.util.stream.Collectors;

public final class JsonUtils {

    public static List<ShopCartVO> parse(Object storeList) {
        return ((JSONArray) storeList).stream().map(store -> {
            JSONObject storeObj = (JSONObject) store;
            ShopCartVO storeTemp = storeObj.toJavaObject(ShopCartVO.class);
            storeTemp.setGoodsList(storeObj.getJSONArray("goodsList").toJavaList(CartGoodsDTO.class));
            return storeTemp;
        }).collect(Collectors.toList());
    }

}
