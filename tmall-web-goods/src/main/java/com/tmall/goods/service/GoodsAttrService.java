package com.tmall.goods.service;

import com.tmall.common.dto.PublicResult;
import com.tmall.goods.entity.dto.GoodsAttrMapDTO;

import java.util.List;
import java.util.Map;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsAttrService {

    List<Map<String, Object>> findGoodsAttrList(int goodsId);

    List<GoodsAttrMapDTO> findAttrList();

    List<GoodsAttrMapDTO> findAttrMapList(int goodsId);

    PublicResult<Integer> saveAttrMap(GoodsAttrMapDTO attrMap);

    PublicResult<?> deleteAttrMap(int id, int goodsId);
}
