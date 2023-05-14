package com.ameshima.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.GlobalConfig;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.LoginInfo;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PageResult;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.common.utils.CheckUtil;
import com.ameshima.common.utils.FileUtil;
import com.ameshima.goods.constants.GoodsErrResultEnum;
import com.ameshima.goods.entity.dto.*;
import com.ameshima.goods.entity.po.*;
import com.ameshima.goods.es.repository.GoodsRepository;
import com.ameshima.goods.keys.GoodsKey;
import com.ameshima.goods.mapper.*;
import com.ameshima.goods.service.GoodsAttrService;
import com.ameshima.goods.service.GoodsCategoryService;
import com.ameshima.goods.service.GoodsService;
import com.ameshima.goods.service.ShoppingCartService;
import com.ameshima.goods.utils.ConvertUtil;
import com.ameshima.remote.goods.dto.CartGoodsDTO;
import com.ameshima.remote.goods.dto.GoodsDTO;
import com.ameshima.remote.goods.dto.OrderAddressDTO;
import com.ameshima.remote.goods.vo.ShopCartVO;
import com.ameshima.remote.order.api.IOrderEvaluateService;
import com.ameshima.remote.order.api.IOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private GlobalConfig globalConfig;
    @Resource
    private RedisClient redisClient;
    @Resource
    private IOrderEvaluateService orderEvaluateService;
    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private GoodsFreightMapper goodsFreightMapper;
    @Resource
    private GoodsImgMapper goodsImgMapper;
    @Resource
    private GoodsParamMapper goodsParamMapper;
    @Resource
    private GoodsSkuMapper goodsSkuMapper;
    @Resource
    private ShoppingCartService shoppingCartService;
    @Resource
    private GoodsAttrService goodsAttrService;
    @Resource
    private GoodsCategoryService goodsCategoryService;
    @Resource
    private IOrderService orderService;

    @Override
    public List<GoodsGridDTO> findByPromote(int promoteId) {
        return goodsMapper.findPromote(promoteId);
    }

    @Override
    public List<GoodsGridDTO> findByCategories(GuessLikeQueryDTO queryParam) {
        int count = Integer.parseInt(globalConfig.get(GlobalConfig.INDEX_GUESS_LIKE_COUNT));
        queryParam.setCount(count);
        List<GoodsGridDTO> goodsList = ArrayUtils.isEmpty(queryParam.getCategories()) ? Lists.newArrayList()
                : goodsMapper.findByCategories(queryParam);
        if (goodsList.size() < count) {
            queryParam.setCount(count - goodsList.size());
            if (goodsList.size() > 0) {
                queryParam.setNotCategories(queryParam.getCategories());
            }
            queryParam.setCategories(null);
            goodsList.addAll(goodsMapper.findByCategories(queryParam));
        }
        return goodsList;
    }

    @Override
    public List<StoreGoodsDTO> storeGoods(int storeId) {
        GoodsQueryDTO query = new GoodsQueryDTO();
        query.setStoreId(storeId);
        query.setIsStoreIndex(MallConstant.YES);
        return redisClient.get(GoodsKey.STORE_INDEX_GOODS, storeId, () -> goodsMapper.storeGoods(query));
    }

    @Override
    public PageResult<GoodsGridDTO> storeGoodsPage(GoodsQueryDTO query) {
        Example example = new Example(GoodsPO.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("storeId",
                query.getStoreId()).andEqualTo("isDelete", MallConstant.NO);
        if (StringUtils.isNotBlank(query.getGoodsName())) {
            criteria.andLike("name", "%" + query.getGoodsName() + "%");
        }
        example.and(criteria);
        example.orderBy("createTime").desc();
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        List<GoodsPO> goodsList = goodsMapper.selectByExample(example);
        PageInfo<GoodsPO> goodsPage = new PageInfo<>(goodsList);
        return new PageResult<>(goodsPage.getPageSize(), goodsPage.getPageNum(), goodsPage.getTotal(),
                ConvertUtil.convertToGrid(goodsList));
    }

    @Override
    public GoodsDTO getGoods(int goodsId) {
        GoodsDTO result = goodsMapper.getGoods(goodsId);
        if (result == null) {
            return null;
        }
        int evaluateCount = 0;
        try {
            evaluateCount = orderEvaluateService.count(goodsId);
        } catch (Exception e) {
            LOGGER.error("Get goods's evaluate count fail, goodsId=>{}", goodsId, e);
        }
        result.setEvaluateCount(evaluateCount);
        return result;
    }

    @Override
    public List<GoodsImgDTO> findImgs(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_IMGS, goodsId, () -> goodsMapper.findImgs(goodsId));
    }

    @Override
    public List<GoodsParamDTO> findParams(int goodsId) {
        return redisClient.get(GoodsKey.GOODS_PARAMS, goodsId, () -> goodsMapper.findParams(goodsId));
    }

    @Override
    public PublicResult<?> saveParam(GoodsParamDTO goodsParam) {
        GoodsParamPO paramPO = new GoodsParamPO(goodsParam.getId(), goodsParam.getGoodsId(),
                goodsParam.getParamName(), goodsParam.getParamValue());
        redisClient.removeKey(GoodsKey.GOODS_PARAMS, goodsParam.getGoodsId());
        if (paramPO.getId() == null && goodsParamMapper.insertSelective(paramPO) == 1) {
            return PublicResult.success(paramPO.getId());
        }
        Example example = new Example(GoodsParamPO.class);
        example.and().andEqualTo("id", paramPO.getId())
                .andEqualTo("goodsId", paramPO.getGoodsId())
                .andEqualTo("isDelete", MallConstant.NO);
        if (goodsParamMapper.updateByExampleSelective(paramPO, example) > 0) {
            return PublicResult.success(paramPO.getId());
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> deleteGoodsParam(int id, int goodsId) {
        if (id < 1 && goodsId < 1) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        GoodsParamPO paramPO = new GoodsParamPO();
        paramPO.setIsDelete(MallConstant.YES);
        redisClient.removeKey(GoodsKey.GOODS_PARAMS, goodsId);
        Example example = new Example(GoodsParamPO.class);
        example.and().andEqualTo("id", id)
                .andEqualTo("goodsId", goodsId)
                .andEqualTo("isDelete", MallConstant.NO);
        if (goodsParamMapper.updateByExampleSelective(paramPO, example) == 1) {
            return PublicResult.success();
        }
        return PublicResult.error();
    }

    @Override
    public List<GoodsSkuDTO> findSku(int goodsId) {
        GoodsSkuDTO param = new GoodsSkuDTO();
        param.setGoodsId(goodsId);
        return goodsMapper.findSku(param);
    }

    @Override
    public PublicResult<?> saveSKU(GoodsSkuDTO goodsSku) {
        GoodsSkuPO skuPO = new GoodsSkuPO(goodsSku.getId(), goodsSku.getGoodsId(), goodsSku.getAttrs(),
                goodsSku.getPrice(), goodsSku.getMarketPrice(), goodsSku.getQuantity());
        Example example = new Example(GoodsSkuPO.class);
        example.and().andEqualTo("goodsId", skuPO.getGoodsId())
                .andEqualTo("attrs", goodsSku.getAttrs())
                .andEqualTo("isDelete", MallConstant.NO);
        if (goodsSku.getId() != null) {
            example.and().andNotEqualTo("id", skuPO.getId());
        }
        if (goodsSkuMapper.selectCountByExample(example) > 0) {
            return PublicResult.error(GoodsErrResultEnum.SKU_REPEAT);
        }
        if (skuPO.getId() == null && goodsSkuMapper.insertSelective(skuPO) == 1) {
            return PublicResult.success(skuPO.getId());
        }
        example.clear();
        example.and().andEqualTo("id", skuPO.getId())
                .andEqualTo("goodsId", skuPO.getGoodsId())
                .andEqualTo("isDelete", MallConstant.NO);
        if (goodsSkuMapper.updateByExampleSelective(skuPO, example) > 0) {
            return PublicResult.success(skuPO.getId());
        }
        return PublicResult.error();
    }

    @Override
    public PublicResult<?> deleteSKU(int id, int goodsId) {
        if (id < 1 && goodsId < 1) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        GoodsSkuPO skuPO = new GoodsSkuPO();
        skuPO.setIsDelete(MallConstant.YES);
        Example example = new Example(GoodsSkuPO.class);
        example.and().andEqualTo("id", id)
                .andEqualTo("goodsId", goodsId)
                .andEqualTo("isDelete", MallConstant.NO);
        if (goodsSkuMapper.updateByExampleSelective(skuPO, example) == 1) {
            return PublicResult.success();
        }
        return PublicResult.error();
    }

    @Override
    public Map<Integer, BigDecimal> getFreight(Set<Integer> goodsIds, String cityCode) {
        if (CollectionUtils.isEmpty(goodsIds) || StringUtils.isBlank(cityCode)) {
            return null;
        }
        Example example = new Example(GoodsFreightPO.class);
        example.and().andEqualTo("targetCityCode", cityCode)
                .andIn("goodsId", goodsIds).andCondition("is_delete=", MallConstant.NO);
        example.setOrderByClause("store_id, dispatch_city_code, cost DESC");
        List<GoodsFreightPO> freightList = goodsFreightMapper.selectByExample(example);
        Table<Integer, String, Map<Integer, BigDecimal>> storeCityFreightMap = HashBasedTable.create();
        BigDecimal zero = new BigDecimal(0);
        // 按每个店铺每个城市收取一次最贵的运费，该店铺该城市下其他商品不收运费
        for (GoodsFreightPO goodsFreight : freightList) {
            Map<Integer, BigDecimal> costMap = storeCityFreightMap.get(goodsFreight.getStoreId(), goodsFreight.getDispatchCityCode());
            if (costMap == null) {
                costMap = new HashMap<>();
            } else {
                goodsFreight.setCost(zero);
            }
            costMap.put(goodsFreight.getGoodsId(), goodsFreight.getCost());
            storeCityFreightMap.put(goodsFreight.getStoreId(), goodsFreight.getDispatchCityCode(), costMap);
        }
        Map<Integer, BigDecimal> freightMap = new HashMap<>();
        storeCityFreightMap.values().forEach(freightMap::putAll);
        for (Integer goodsId : goodsIds) {
            if (!freightMap.containsKey(goodsId)) {
                freightMap.put(goodsId, zero);
            }
        }
        return freightMap;
    }

    @Override
    public PageResult<EsGoodsDTO> indexGoods(QueryGoodsDTO queryParam) {
        Assert.isTrue(queryParam != null && StringUtils.isNotBlank(queryParam.getWord()), MallConstant.PARAM_ERR_MSG);
        NativeSearchQueryBuilder searchQueryBuilder = buildEsSearch(queryParam);
        if (StringUtils.isNotBlank(queryParam.getOrderField())) {
            searchQueryBuilder.withSort(SortBuilders.fieldSort(queryParam.getOrderField())
                    .order("ASC".equals(queryParam.getOrderType()) ? SortOrder.ASC : SortOrder.DESC));
        }
        Page<EsGoodsDTO> page = goodsRepository.search(searchQueryBuilder.build());
        PageResult<EsGoodsDTO> result = new PageResult<>();
        result.setContent(page.getContent());
        result.setPageIndex(page.getNumber());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public Map<String, Set<String>> findBrandsAndCategories(QueryGoodsDTO queryParam) {
        Assert.isTrue(queryParam != null && StringUtils.isNotBlank(queryParam.getWord()), MallConstant.PARAM_ERR_MSG);
        queryParam.setPageIndex(0);
        NativeSearchQueryBuilder searchQueryBuilder = buildEsSearch(queryParam);
        searchQueryBuilder.withFields("brand", "categories");
        searchQueryBuilder.addAggregation(AggregationBuilders.terms("goods").field("id"));
        Page<EsGoodsDTO> searchResult = goodsRepository.search(searchQueryBuilder.build());
        Map<String, Set<String>> result = Maps.newTreeMap();
        Set<String> brandSet = Sets.newHashSet(), categorySet = Sets.newHashSet();
        result.put("brands", brandSet);
        result.put("categories", categorySet);
        for (EsGoodsDTO goods : searchResult) {
            brandSet.add(goods.getBrand());
            categorySet.addAll(Sets.newHashSet(goods.getCategories().split(",")));
        }
        return result;
    }

    @Override
    public PublicResult<?> cacheBuySkus(List<ShoppingCartDTO> skuList) {
        PublicResult<?> result = CheckUtil.isTrue(!CollectionUtils.isEmpty(skuList));
        if (result != null) {
            return result;
        }
        Map<Integer, ShoppingCartDTO> skuMap = skuList.stream().collect(Collectors.toMap(ShoppingCartDTO::getSkuId, shoppingCartDTO -> shoppingCartDTO));
        List<CartGoodsDTO> goodsList = goodsMapper.goodsBySkus(skuMap.keySet());
        result = CheckUtil.isTrue(!CollectionUtils.isEmpty(goodsList));
        if (result != null) {
            return result;
        }
        for (CartGoodsDTO goods : goodsList) {
            ShoppingCartDTO cartTemp = skuMap.get(goods.getSkuId());
            if (cartTemp.getAmount() > goods.getQuantity()) {
                return PublicResult.error(GoodsErrResultEnum.AMOUNT_OVER);
            }
            goods.setId(cartTemp.getCartId());
            goods.setAttrsJson(cartTemp.getAttrsJson());
            goods.setAmount(cartTemp.getAmount());
        }
        if (redisClient.set(GoodsKey.USER_BUY_SKUS, LoginInfo.get().getAccountId(), goodsList)) {
            return PublicResult.success();
        }
        return PublicResult.error();
    }

    @Override
    @Transactional
    public PublicResult<?> skuOrdered(int accountId) {
        Assert.isTrue(accountId > 0, CommonErrResult.ERR_REQUEST.errMsg());
        List<CartGoodsDTO> skuList = redisClient.get(GoodsKey.USER_BUY_SKUS, accountId);
        // 在庫を減らす
        for (CartGoodsDTO sku : skuList) {
            if (goodsSkuMapper.quantityDown(sku) == 0) {
                throw new IllegalArgumentException(PublicResult.error(GoodsErrResultEnum.AMOUNT_OVER).toString());
            }
        }
        LOGGER.info("accountId->{}は注文して在庫を減らした。注文内容->{}", accountId, JSON.toJSON(skuList));
        // ショップカートを削除
        Set<Integer> cartIds = skuList.stream().map(CartGoodsDTO::getId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (!CollectionUtils.isEmpty(cartIds)) {
            PublicResult<?> removeCartRes = shoppingCartService.remove(cartIds, accountId);
            if (removeCartRes.getErrCode() != MallConstant.NO) {
                throw new IllegalArgumentException(removeCartRes.toString());
            }
        }
        // 買った商品のキャッシュを削除
        if (!redisClient.removeKey(GoodsKey.USER_BUY_SKUS, accountId)) {
            throw new IllegalArgumentException(PublicResult.error().toString());
        }
        return PublicResult.success();
    }

    @Override
    public PublicResult<?> updateCacheBuySkusAmount(int skuId, int amount) {
        PublicResult<?> result = CheckUtil.isTrue(skuId > 0 && amount > 0);
        if (result != null) {
            return result;
        }
        List<CartGoodsDTO> skus = redisClient.get(GoodsKey.USER_BUY_SKUS, LoginInfo.get().getAccountId());
        result = CheckUtil.notEmpty(skus, GoodsErrResultEnum.BUY_CACHE_NOT_EXISTS);
        if (result != null) {
            return result;
        }
        GoodsSkuPO goodsSku = goodsSkuMapper.selectByPrimaryKey(skuId);
        result = CheckUtil.notNull(goodsSku);
        if (result != null) {
            return result;
        }
        if (goodsSku.getIsDelete() == MallConstant.NO || amount > goodsSku.getQuantity()) {
            return PublicResult.error(GoodsErrResultEnum.AMOUNT_OVER);
        }
        boolean isChange = false;
        for (CartGoodsDTO goods : skus) {
            if (skuId == goods.getSkuId()) {
                goods.setAmount(amount);
                goods.setQuantity(goodsSku.getQuantity());
                isChange = true;
                break;
            }
        }
        if (!isChange) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        if (redisClient.set(GoodsKey.USER_BUY_SKUS, LoginInfo.get().getAccountId(), skus)) {
            return PublicResult.success();
        }
        return PublicResult.error();
    }

    @Override
    public List<ShopCartVO> goodsBySkus(OrderAddressDTO addressDTO) {
        List<CartGoodsDTO> goodsList = redisClient.get(GoodsKey.USER_BUY_SKUS, addressDTO.getAccountId());
        if (CollectionUtils.isEmpty(goodsList)) {
            return Collections.emptyList();
        }
        Map<Integer, BigDecimal> freightMap = new HashMap<>();
        if (StringUtils.isNotBlank(addressDTO.getCityCode())) {
            freightMap = getFreight(goodsList.stream().map(CartGoodsDTO::getGoodsId).collect(Collectors.toSet()), addressDTO.getCityCode());
        }
        Map<ShopCartVO, ShopCartVO> cartMap = Maps.newLinkedHashMap();
        ShopCartVO shopCart, temp;
        for (CartGoodsDTO goods : goodsList) {
            goods.setFreight(freightMap.get(goods.getGoodsId()));
            temp = new ShopCartVO(goods.getStoreId(), goods.getStoreName(), goods.getState());
            shopCart = cartMap.get(temp);
            if (shopCart == null) {
                shopCart = temp;
                shopCart.setGoodsList(Lists.newArrayList());
                cartMap.put(temp, shopCart);
            }
            shopCart.getGoodsList().add(goods);
        }
        return new ArrayList<>(cartMap.values());
    }

    @Override
    public StoreGoodsDTO goodsDetail(int goodsId) {
        StoreGoodsDTO storeGoods = goodsMapper.goodsDetail(goodsId, LoginInfo.get().getStoreId());
        storeGoods.setImgList(goodsImgMapper.goodsImgList(goodsId));
        List<GoodsAttrMapDTO> attrMapList = goodsAttrService.findAttrMapList(goodsId);
        Map<Integer, String> attrMap = Maps.newHashMap();
        for (GoodsAttrMapDTO attrMapDTO : attrMapList) {
            attrMap.put(attrMapDTO.getId(), attrMapDTO.getTxtValue());
        }
        List<GoodsSkuDTO> skuList = findSku(goodsId);
        String[] attrAry;
        StringJoiner joiner;
        for (GoodsSkuDTO sku : skuList) {
            attrAry = sku.getAttrs().split(",");
            joiner = new StringJoiner(" ");
            for (String attrId : attrAry) {
                joiner.add(attrMap.get(Integer.parseInt(attrId)));
            }
            sku.setAttrs(joiner.toString());
        }
        storeGoods.setSkuList(skuList);
        return storeGoods;
    }

    @Override
    public PublicResult<?> withdrawGoods(int goodsId) {
        Integer storeId = LoginInfo.get().getStoreId();
        if (storeId == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        // 支払ってない注文チェック
        if (orderService.orderGoodsExists(goodsId, MallConstant.OrderStateEnum.NO_PAY.getState())) {
            return PublicResult.error(GoodsErrResultEnum.NO_PAY_ORDER_EXISTS);
        }
        if (stackOrWithdrawGoods(goodsId, storeId, MallConstant.NO)) {
            // elasticsearchから商品を削除
            goodsRepository.delete(goodsId);
        }
        return PublicResult.success();
    }

    @Override
    public PublicResult<?> stackGoods(int goodsId) {
        Integer storeId = LoginInfo.get().getStoreId();
        if (storeId == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        // 情報チェック
        Example example = new Example(GoodsPO.class);
        example.and().andEqualTo("id", goodsId)
                .andIsNotNull("skuId");
        if (goodsMapper.selectCountByExample(example) == 0) {
            return PublicResult.error(GoodsErrResultEnum.NO_SKU);
        }
        if (stackOrWithdrawGoods(goodsId, storeId, MallConstant.YES)) {
            // elasticsearchに商品を保存
            List<EsGoodsDTO> esGoodsList = goodsMapper.findEsGoods(goodsId);
            goodsRepository.save(esGoodsList);
            return PublicResult.success();
        }
        return PublicResult.error(CommonErrResult.OPERATE_FAIL);
    }

    private boolean stackOrWithdrawGoods(int goodsId, Integer storeId, short status) {
        redisClient.removeKey(GoodsKey.INDEX_PROMOTE_PLATE, null);
        redisClient.removeKey(GoodsKey.STORE_INDEX_GOODS, storeId);
        redisClient.removeKey(GoodsKey.GOODS_ATTRS, goodsId);
        redisClient.removeKey(GoodsKey.GOODS_PARAMS, goodsId);
        redisClient.removeKey(GoodsKey.GOODS_IMGS, goodsId);
        GoodsPO goodsPO = new GoodsPO();
        goodsPO.setStatus(status);
        Example example = new Example(GoodsPO.class);
        example.and().andEqualTo("id", goodsId)
                .andEqualTo("storeId", storeId)
                .andEqualTo("status", MallConstant.YES - status)
                .andEqualTo("isDelete", MallConstant.NO);
        return goodsMapper.updateByExampleSelective(goodsPO, example) > 0;
    }

    @Override
    public PublicResult<?> saveStoreGoods(StoreGoodsDTO storeGoods) {
        if (LoginInfo.get().getStoreId() == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        try {
            if (saveStoreGoodsWithTransactional(storeGoods) == 1) return PublicResult.success();
        } catch (Exception e) {
            LOGGER.error(String.format("商品情報の保存がエラー=>%s", JSON.toJSONString(storeGoods)), e);
        }
        return PublicResult.error();
    }

    @Transactional
    int saveStoreGoodsWithTransactional(StoreGoodsDTO storeGoods) throws IOException {
        int result;
        GoodsPO goodsPO = new GoodsPO();
        LoginUser loginUser = LoginInfo.get();
        goodsPO.setId(storeGoods.getId());
        goodsPO.setStoreId(loginUser.getStoreId());
        goodsPO.setStoreName(loginUser.getStoreName());
        // goodsPO.setBrandId();
        goodsPO.setSkuId(storeGoods.getSkuId());
        goodsPO.setName(storeGoods.getName());
        goodsPO.setSimpleDesc(storeGoods.getSimpleDesc());
        goodsPO.setPrice(storeGoods.getPrice());
        goodsPO.setPromoPrice(storeGoods.getPromoPrice());
        goodsPO.setImgUrl(FileUtil.compressImgToBase64(storeGoods.getImgList().get(0).getImgUrl()));
        goodsPO.setLocation(storeGoods.getLocation());
        goodsPO.setIsShowBanner(storeGoods.getIsShowBanner());
        goodsPO.setIsPromote(storeGoods.getIsPromote());
        Example example;
        if (storeGoods.getId() == 0) {
            // 商品を作る
            result = goodsMapper.insertSelective(goodsPO);
        } else {
            // 商品を変える
            example = new Example(GoodsPO.class);
            example.and().andEqualTo("id", goodsPO.getId())
                    .andEqualTo("storeId", loginUser.getStoreId())
                    .andEqualTo("status", MallConstant.NO)
                    .andEqualTo("isDelete", MallConstant.NO);
            result = goodsMapper.updateByExampleSelective(goodsPO, example);
        }
        if (result != 1) {
            return result;
        }
        // カテゴリー関連を変える
        goodsCategoryService.saveGoodsCategoryRelation(goodsPO.getId(), storeGoods.getCategoryId4());
        //写真を変える
        GoodsImgPO goodsImgPO = new GoodsImgPO();
        goodsImgPO.setIsDelete(MallConstant.YES);
        example = new Example(GoodsImgPO.class);
        example.and().andEqualTo("goodsId", goodsPO.getId())
                .andEqualTo("isDelete", MallConstant.NO);
        goodsImgMapper.updateByExampleSelective(goodsImgPO, example);
        Date now = new Date();
        List<GoodsImgPO> imgList = storeGoods.getImgList().stream().map(goodsImgDTO -> {
            GoodsImgPO imgPO = new GoodsImgPO();
            imgPO.setGoodsId(goodsPO.getId());
            imgPO.setImgType(goodsImgDTO.getImgType());
            try {
                imgPO.setImgUrl(FileUtil.compressImgToBase64(goodsImgDTO.getImgUrl()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imgPO.setCreateTime(now);
            imgPO.setIsDelete(MallConstant.NO);
            return imgPO;
        }).collect(Collectors.toList());
        goodsImgMapper.insertList(imgList);
        return result;
    }

    /**
     * 构建ES查询对象
     *
     * @param queryParam 查询参数
     * @return ES查询对象
     */
    private NativeSearchQueryBuilder buildEsSearch(QueryGoodsDTO queryParam) {
        MatchQueryBuilder cateBuilder = QueryBuilders.matchPhraseQuery("allCategories", queryParam.getWord());
        MatchQueryBuilder brandBuilder = QueryBuilders.matchPhraseQuery("brand", queryParam.getWord());
        MatchQueryBuilder nameBuilder = QueryBuilders.matchPhraseQuery("name", queryParam.getWord());
        MatchQueryBuilder storeBuilder = QueryBuilders.matchPhraseQuery("store", queryParam.getWord());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.boolQuery().should(cateBuilder)
                .should(brandBuilder).should(nameBuilder).should(storeBuilder));
        if (queryParam.getStoreId() != null && queryParam.getStoreId() > 0) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("storeId", queryParam.getStoreId()));
        }
        if (StringUtils.isNotBlank(queryParam.getBrand())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("brand", queryParam.getBrand()));
        }
        if (StringUtils.isNotBlank(queryParam.getCategory())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("allCategories", queryParam.getCategory()));
        }
        if (queryParam.getMinPrice() != null || queryParam.getMaxPrice() != null) {
            RangeQueryBuilder priceRange = QueryBuilders.rangeQuery("price").from(queryParam.getMinPrice())
                    .to(queryParam.getMaxPrice());
            boolQueryBuilder.must(priceRange);
        }
        int pageSize = NumberUtils.toInt(globalConfig.get(GlobalConfig.SEARCH_PAGE_SIZE), 60);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(boolQueryBuilder)
                .withPageable(new PageRequest(queryParam.getPageIndex(), pageSize));
        return searchQueryBuilder;
    }
}
