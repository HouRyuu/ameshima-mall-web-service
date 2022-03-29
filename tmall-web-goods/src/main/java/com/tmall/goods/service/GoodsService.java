package com.tmall.goods.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.goods.entity.dto.*;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.vo.ShopCartVO;
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

    Map<Integer, BigDecimal> getFreight(Set<Integer> goodsIds, String cityCode);

    PageResult<EsGoodsDTO> indexGoods(QueryGoodsDTO queryParam);

    Map<String, Set<String>> findBrandsAndCategories(QueryGoodsDTO queryParam);

    PublicResult<?>  cacheBuySkus(List<ShoppingCartDTO> skuList);

    PublicResult<?> skuOrdered(int accountId);

    PublicResult<?>  updateCacheBuySkusAmount(int skuId, int amount);

    List<ShopCartVO> goodsBySkus(OrderAddressDTO addressDTO);
}
