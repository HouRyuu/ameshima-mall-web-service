package com.tmall.goods.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.goods.entity.dto.GoodsAttrMapDTO;
import com.tmall.goods.entity.po.GoodsAttrMapPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsAttrMapMapper extends BaseMapper<GoodsAttrMapPO> {

    List<GoodsAttrMapDTO> findGoodsAttrList(@Param("goodsId") int goodsId);

}
