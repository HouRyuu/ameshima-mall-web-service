package com.tmall.goods.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.goods.entity.dto.GoodsImgDTO;
import com.tmall.goods.entity.po.GoodsImgPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsImgMapper extends BaseMapper<GoodsImgPO> {

    List<GoodsImgDTO> goodsImgList(@Param("goodsId") int goodsId);
}
