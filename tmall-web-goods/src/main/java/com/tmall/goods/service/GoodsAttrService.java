package com.tmall.goods.service;

import com.tmall.common.dto.PublicResult;
import com.tmall.goods.entity.dto.GoodsAttrMapDTO;

import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsAttrService {

    List<Map<String, Object>> findGoodsAttrList(int goodsId);

    List<GoodsAttrMapDTO> findAttrList();

    List<GoodsAttrMapDTO> findAttrMapList(int goodsId);

    PublicResult<Integer> saveAttrMap(GoodsAttrMapDTO attrMap);

    PublicResult<?> deleteAttrMap(int id, int goodsId);
}
