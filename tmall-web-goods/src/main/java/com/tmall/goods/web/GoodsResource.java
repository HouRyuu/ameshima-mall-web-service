package com.tmall.goods.web;

import java.util.List;
import java.util.Map;

import com.tmall.goods.constants.GoodsErrResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.dto.AjaxResult;
import com.tmall.goods.entity.dto.GoodsImgDTO;
import com.tmall.goods.entity.dto.GuessLikeQueryDTO;
import com.tmall.goods.entity.dto.QueryGoodsDTO;
import com.tmall.goods.service.GoodsAttrService;
import com.tmall.goods.service.GoodsCategoryService;
import com.tmall.goods.service.GoodsPromoteService;
import com.tmall.goods.service.GoodsService;
import com.tmall.remote.goods.dto.GoodsDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
public class GoodsResource {

    @Autowired
    private GoodsCategoryService goodsCategoryService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsPromoteService goodsPromoteService;
    @Autowired
    private GoodsAttrService goodsAttrService;

    @GetMapping("/getGoods/{id}")
    public GoodsDTO getGoods(@PathVariable Integer id) {
        GoodsDTO goods = new GoodsDTO();
        goods.setId(id);
        goods.setName("Prada 2019夏季男士短袖");
        return goods;
    }

    @GetMapping("/findSecondCategories")
    public AjaxResult findSecondCategories() {
        return AjaxResult.success(goodsCategoryService.findSecondCategories());
    }

    @GetMapping("/{pid}/findCategories")
    public AjaxResult findCategoriesByPid(@PathVariable int pid) {
        return AjaxResult.success(goodsCategoryService.findCategoriesByPid(pid));
    }

    @GetMapping("/indexPromotePlate")
    public AjaxResult indexPromotePlate() {
        return AjaxResult.success(goodsPromoteService.findPromotes());
    }

    @GetMapping("/{promoteId}/findPromoteGoods")
    public AjaxResult findPromoteGoods(@PathVariable int promoteId) {
        return AjaxResult.success(goodsService.findByPromote(promoteId));
    }

    @PostMapping("/guessLike")
    public AjaxResult guessLike(@RequestBody GuessLikeQueryDTO queryParam) {
        return AjaxResult.success(goodsService.findByCategories(queryParam));
    }

    @GetMapping("/{storeId}/storeGoods")
    public AjaxResult storeGoods(@PathVariable int storeId) {
        return AjaxResult.success(goodsService.storeGoods(storeId));
    }

    @GetMapping("/{goodsId}/detail")
    public AjaxResult detail(@PathVariable int goodsId) {
        Map<String, Object> result = Maps.newHashMap();
        GoodsDTO goods = goodsService.getGoods(goodsId);
        if (goods == null) {
            return AjaxResult.error(GoodsErrResultEnum.GOODS_NOT_EXISTS);
        }
        result.put("goods", goods);
        result.put("attrs", goodsAttrService.findGoodsAttrList(goodsId));
        result.put("skus", goodsService.findSku(goodsId));
        List<GoodsImgDTO> imgList = goodsService.findImgs(goodsId);
        List<String> coverImgs = Lists.newArrayList(), detailImgs = Lists.newArrayList();
        for (GoodsImgDTO img : imgList) {
            if (img.getImgType() == GlobalConfig.GOODS_IMG_TYPE_COVER) {
                coverImgs.add(img.getImgUrl());
                continue;
            }
            detailImgs.add(img.getImgUrl());
        }
        result.put("coverImgs", coverImgs);
        result.put("detailImgs", detailImgs);
        result.put("params", goodsService.findParams(goodsId));
        return AjaxResult.success(result);
    }

    @GetMapping("/{goodsId}/{cityCode}/freight")
    public AjaxResult freight(@PathVariable int goodsId, @PathVariable String cityCode) {
        return AjaxResult.success(goodsService.getFreight(goodsId, cityCode));
    }

    @PostMapping("/indexGoods")
    public AjaxResult indexGoods(@RequestBody QueryGoodsDTO queryParam) {
        return AjaxResult.success(goodsService.indexGoods(queryParam));
    }

    @PostMapping("/findBrandsAndCategories")
    public AjaxResult findBrandsAndCategories(@RequestBody QueryGoodsDTO queryParam) {
        return AjaxResult.success(goodsService.findBrandsAndCategories(queryParam));
    }

}
