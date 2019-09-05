package com.tmall.goods.service;

import java.util.List;

import com.tmall.goods.entity.dto.*;
import com.tmall.remote.goods.dto.GoodsDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface GoodsService {

    List<GoodsGridDTO> findByPromote(int promoteId);

    List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam);

    List<StoreGoodsDTO> storeGoods(int storeId);

    GoodsDTO getGoods(int goodsId);

    List<GoodsImgDTO> findImgs(int goodsId);

    List<GoodsParamDTO> findParams(int goodsId);

    List<GoodsSkuDTO> findSku(int goodsId);

    float getFreight(int goodsId, String cityCode);

}
