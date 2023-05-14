package com.ameshima.goods.mapper;

import com.ameshima.goods.entity.dto.*;
import com.ameshima.common.BaseMapper;
import com.ameshima.goods.entity.po.GoodsPO;
import com.ameshima.remote.goods.dto.CartGoodsDTO;
import com.ameshima.remote.goods.dto.GoodsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsMapper extends BaseMapper<GoodsPO> {

    GoodsDTO getGoods(@Param("goodsId") int goodsId);

    List<GoodsGridDTO> findPromote(@Param("promoteId") int promoteId);

    List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam);

    List<StoreGoodsDTO> storeGoods(GoodsQueryDTO query);

    List<GoodsImgDTO> findImgs(@Param("goodsId") int goodsId);

    List<GoodsParamDTO> findParams(@Param("goodsId") int goodsId);

    List<GoodsSkuDTO> findSku(GoodsSkuDTO param);

    List<EsGoodsDTO> findEsGoods(@Param("goodsId") int goodsId);

    List<CartGoodsDTO> goodsBySkus(@Param("skuIds") Set<Integer> skuIds);

    StoreGoodsDTO goodsDetail(@Param("goodsId") int goodsId, @Param("storeId") int storeId);

}
