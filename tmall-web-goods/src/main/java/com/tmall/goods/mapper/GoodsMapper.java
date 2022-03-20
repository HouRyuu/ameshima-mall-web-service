package com.tmall.goods.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.goods.entity.dto.*;
import com.tmall.goods.entity.po.GoodsPO;
import com.tmall.remote.goods.dto.GoodsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsMapper extends BaseMapper<GoodsPO> {

    GoodsDTO getGoods(@Param("goodsId") int goodsId);

    List<GoodsGridDTO> findPromote(@Param("promoteId") int promoteId);

    List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam);

    List<StoreGoodsDTO> storeGoods(@Param("storeId") int storeId);

    List<GoodsImgDTO> findImgs(@Param("goodsId") int goodsId);

    List<GoodsParamDTO> findParams(@Param("goodsId") int goodsId);

    List<GoodsSkuDTO> findSku(GoodsSkuDTO param);

    Float getFreight(@Param("goodsId") int goodsId, @Param("cityCode") String cityCode);

    List<EsGoodsDTO> findEsGoods();

    List<CartGoodsDTO> goodsBySkus(@Param("skuIds") Set<Integer> skuIds);

}
