package com.ameshima.goods.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.goods.entity.dto.GoodsImgDTO;
import com.ameshima.goods.entity.po.GoodsImgPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsImgMapper extends BaseMapper<GoodsImgPO> {

    List<GoodsImgDTO> goodsImgList(@Param("goodsId") int goodsId);
}
