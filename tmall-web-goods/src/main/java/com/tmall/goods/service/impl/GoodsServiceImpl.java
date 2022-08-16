package com.tmall.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.*;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.PageResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.utils.CheckUtil;
import com.tmall.goods.constants.GoodsErrResultEnum;
import com.tmall.goods.entity.dto.*;
import com.tmall.goods.entity.po.GoodsFreightPO;
import com.tmall.goods.entity.po.GoodsSkuPO;
import com.tmall.goods.es.repository.GoodsRepository;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsFreightMapper;
import com.tmall.goods.mapper.GoodsMapper;
import com.tmall.goods.mapper.GoodsSkuMapper;
import com.tmall.goods.service.GoodsService;
import com.tmall.goods.service.ShoppingCartService;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.dto.CartGoodsDTO;
import com.tmall.remote.goods.dto.GoodsDTO;
import com.tmall.remote.goods.vo.ShopCartVO;
import com.tmall.remote.order.api.IOrderEvaluateService;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
    private GoodsSkuMapper goodsSkuMapper;
    @Resource
    private ShoppingCartService shoppingCartService;


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
        return redisClient.get(GoodsKey.STORE_INDEX_GOODS, storeId, () -> goodsMapper.storeGoods(storeId));
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
    public List<GoodsSkuDTO> findSku(int goodsId) {
        GoodsSkuDTO param = new GoodsSkuDTO();
        param.setGoodsId(goodsId);
        return goodsMapper.findSku(param);
    }

    @Override
    public Map<Integer, BigDecimal> getFreight(Set<Integer> goodsIds, String cityCode) {
        if (CollectionUtils.isEmpty(goodsIds) || StringUtils.isBlank(cityCode)) {
            return null;
        }
        Example example = new Example(GoodsFreightPO.class);
        example.and().andEqualTo("targetCityCode", cityCode)
                .andIn("goodsId", goodsIds).andCondition("is_delete=", TmallConstant.NO);
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
        Assert.isTrue(queryParam != null && StringUtils.isNotBlank(queryParam.getWord()), TmallConstant.PARAM_ERR_MSG);
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
        Assert.isTrue(queryParam != null && StringUtils.isNotBlank(queryParam.getWord()), TmallConstant.PARAM_ERR_MSG);
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
            if (removeCartRes.getErrCode() != TmallConstant.NO) {
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
        if (goodsSku.getIsDelete() == TmallConstant.NO || amount > goodsSku.getQuantity()) {
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
