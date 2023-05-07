package com.tmall.goods.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.tmall.common.dto.PageResult;
import com.tmall.goods.entity.dto.EsGoodsDTO;
import com.tmall.goods.entity.dto.QueryGoodsDTO;
import com.tmall.goods.es.repository.GoodsRepository;
import com.tmall.goods.mapper.GoodsMapper;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {

    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private GoodsMapper goodsMapper;

    @Before
    public void initGoods() {
        goodsRepository.deleteAll();
        List<EsGoodsDTO> esGoodsList = goodsMapper.findEsGoods(0);
        goodsRepository.save(esGoodsList);
    }

    @Test
    public void indexGoods() {
        QueryGoodsDTO queryParam = new QueryGoodsDTO();
        queryParam.setStoreId(38);
        queryParam.setWord("Apple");
        queryParam.setPageIndex(0);
        // queryParam.setOrderField("price");
        // queryParam.setOrderType("DESC");
        // queryParam.setMinPrice(new BigDecimal(6000));
        queryParam.setBrand("Apple");
        // queryParam.setCategory("手机");
        PageResult<EsGoodsDTO> esGoodsDTOS = goodsService.indexGoods(queryParam);
        System.out.println(JSON.toJSONString(esGoodsDTOS));
        Map<String, Set<String>> brandsAndCategories = goodsService.findBrandsAndCategories(queryParam);
        System.out.println(JSON.toJSONString(brandsAndCategories));
    }

}