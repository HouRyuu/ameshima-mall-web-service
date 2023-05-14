package com.ameshima.goods.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ameshima.goods.entity.dto.*;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.remote.goods.dto.OrderAddressDTO;
import com.ameshima.remote.goods.vo.ShopCartVO;
import com.ameshima.remote.goods.dto.GoodsDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsService {

    List<GoodsGridDTO> findByPromote(int promoteId);

    List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam);

    List<StoreGoodsDTO> storeGoods(int storeId);

    PageResult<GoodsGridDTO> storeGoodsPage(GoodsQueryDTO query);

    GoodsDTO getGoods(int goodsId);

    List<GoodsImgDTO> findImgs(int goodsId);

    List<GoodsParamDTO> findParams(int goodsId);

    PublicResult<?> saveParam(GoodsParamDTO goodsParam);

    PublicResult<?> deleteGoodsParam(int id, int goodsId);

    List<GoodsSkuDTO> findSku(int goodsId);

    PublicResult<?> saveSKU(GoodsSkuDTO goodsSku);

    PublicResult<?> deleteSKU(int id, int goodsId);

    Map<Integer, BigDecimal> getFreight(Set<Integer> goodsIds, String cityCode);

    PageResult<EsGoodsDTO> indexGoods(QueryGoodsDTO queryParam);

    Map<String, Set<String>> findBrandsAndCategories(QueryGoodsDTO queryParam);

    PublicResult<?>  cacheBuySkus(List<ShoppingCartDTO> skuList);

    PublicResult<?> skuOrdered(int accountId);

    PublicResult<?>  updateCacheBuySkusAmount(int skuId, int amount);

    List<ShopCartVO> goodsBySkus(OrderAddressDTO addressDTO);

    StoreGoodsDTO goodsDetail(int goodsId);

    PublicResult<?> withdrawGoods(int goodsId);

    PublicResult<?> stackGoods(int goodsId);

    PublicResult<?> saveStoreGoods(StoreGoodsDTO storeGoods);
}
