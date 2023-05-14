package com.ameshima.goods.mapper;

import com.ameshima.common.BaseMapper;
import com.ameshima.goods.entity.dto.GoodsAttrMapDTO;
import com.ameshima.goods.entity.po.GoodsAttrMapPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsAttrMapMapper extends BaseMapper<GoodsAttrMapPO> {

    List<GoodsAttrMapDTO> findGoodsAttrList(@Param("goodsId") int goodsId);

}
