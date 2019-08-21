package com.tmall.goods.service;

import java.util.Collection;
import java.util.Map;

import com.tmall.goods.entity.dto.GoodsAttrDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsAttrService {

    Map<String, Collection<GoodsAttrDTO>> findGoodsAttrList(int goodsId);
}
