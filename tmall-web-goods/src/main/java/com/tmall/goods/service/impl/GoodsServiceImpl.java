package com.tmall.goods.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.PageResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.goods.entity.dto.*;
import com.tmall.goods.es.repository.GoodsRepository;
import com.tmall.goods.keys.GoodsKey;
import com.tmall.goods.mapper.GoodsMapper;
import com.tmall.goods.service.GoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;
import com.tmall.remote.order.api.IOrderEvaluateService;

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

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GlobalConfig globalConfig;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private IOrderEvaluateService orderEvaluateService;
    @Autowired
    private GoodsRepository goodsRepository;

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
        return goodsMapper.findSku(goodsId);
    }

    @Override
    public float getFreight(int goodsId, String cityCode) {
        Float freight = goodsMapper.getFreight(goodsId, cityCode);
        return freight == null ? 0 : freight;
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
