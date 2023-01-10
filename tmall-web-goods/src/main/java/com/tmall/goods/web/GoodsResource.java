package com.tmall.goods.web;

import java.util.*;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.constants.UserErrResultEnum;
import com.tmall.common.dto.LoginInfo;
import com.tmall.goods.constants.GoodsErrResultEnum;
import com.tmall.goods.entity.dto.*;
import com.tmall.remote.goods.api.IGoodsService;
import com.tmall.remote.goods.dto.OrderAddressDTO;
import com.tmall.remote.goods.vo.ShopCartVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.dto.PublicResult;
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
public class GoodsResource implements IGoodsService {

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
    public PublicResult<?> findSecondCategories() {
        return PublicResult.success(goodsCategoryService.findSecondCategories());
    }

    @GetMapping("/{pid}/findChildrenCategories")
    public PublicResult<?> findChildrenCategories(@PathVariable int pid) {
        return PublicResult.success(goodsCategoryService.findChildrenCategories(pid));
    }

    @GetMapping("/{pid}/findCategories")
    public PublicResult<?> findCategoriesByPid(@PathVariable int pid) {
        return PublicResult.success(goodsCategoryService.findCategoriesByPid(pid));
    }

    @GetMapping("/indexPromotePlate")
    public PublicResult<?> indexPromotePlate() {
        return PublicResult.success(goodsPromoteService.findPromotes());
    }

    @GetMapping("/{promoteId}/findPromoteGoods")
    public PublicResult<?> findPromoteGoods(@PathVariable int promoteId) {
        return PublicResult.success(goodsService.findByPromote(promoteId));
    }

    @PostMapping("/guessLike")
    public PublicResult<?> guessLike(@RequestBody GuessLikeQueryDTO queryParam) {
        return PublicResult.success(goodsService.findByCategories(queryParam));
    }

    @GetMapping("/{storeId}/storeGoods")
    public PublicResult<?> storeGoods(@PathVariable int storeId) {
        return PublicResult.success(goodsService.storeGoods(storeId));
    }

    @PostMapping("/store/page")
    public PublicResult<?> storeGoodsPage(@RequestBody GoodsQueryDTO query) {
        if (LoginInfo.get() != null && LoginInfo.get().getStoreId() != null) {
            query.setStoreId(LoginInfo.get().getStoreId());
        }
        return PublicResult.success(goodsService.storeGoodsPage(query));
    }

    @GetMapping("/{goodsId}/detail")
    public PublicResult<?> detail(@PathVariable int goodsId) {
        Map<String, Object> result = Maps.newHashMap();
        GoodsDTO goods = goodsService.getGoods(goodsId);
        if (goods == null) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
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
        return PublicResult.success(result);
    }

    @LoginRequire
    @GetMapping("/{goodsId}/param/list")
    public PublicResult<?> paramList(@PathVariable int goodsId) {
        return PublicResult.success(goodsService.findParams(goodsId));
    }

    @LoginRequire
    @PutMapping("/param/save")
    public PublicResult<?> saveParam(@RequestBody GoodsParamDTO goodsParam) {
        return goodsService.saveParam(goodsParam);
    }

    @LoginRequire
    @DeleteMapping("/{goodsId}/param/{id}/delete")
    public PublicResult<?> deleteParam(@PathVariable int goodsId, @PathVariable int id) {
        return goodsService.deleteGoodsParam(id, goodsId);
    }

    @LoginRequire
    @GetMapping("/{goodsId}/sku/list")
    public PublicResult<?> skuList(@PathVariable int goodsId) {
        return PublicResult.success(goodsService.findSku(goodsId));
    }

    @LoginRequire
    @PutMapping("/sku/save")
    public PublicResult<?> saveSKU(@RequestBody GoodsSkuDTO goodsSKU) {
        return goodsService.saveSKU(goodsSKU);
    }

    @LoginRequire
    @DeleteMapping("/{goodsId}/sku/{id}/delete")
    public PublicResult<?> deleteSKU(@PathVariable int goodsId, @PathVariable int id) {
        return goodsService.deleteSKU(id, goodsId);
    }

    @PostMapping("/{cityCode}/freight")
    public PublicResult<?> freight(@RequestBody Set<Integer> goodsIds, @PathVariable String cityCode) {
        return PublicResult.success(goodsService.getFreight(goodsIds, cityCode));
    }

    @PostMapping("/indexGoods")
    public PublicResult<?> indexGoods(@RequestBody QueryGoodsDTO queryParam) {
        return PublicResult.success(goodsService.indexGoods(queryParam));
    }

    @PostMapping("/findBrandsAndCategories")
    public PublicResult<?> findBrandsAndCategories(@RequestBody QueryGoodsDTO queryParam) {
        return PublicResult.success(goodsService.findBrandsAndCategories(queryParam));
    }

    @LoginRequire
    @PutMapping("/cacheBuySkus")
    public PublicResult<?> cacheBuySkus(@RequestBody List<ShoppingCartDTO> skuList) {
        return goodsService.cacheBuySkus(skuList);
    }

    @LoginRequire
    @RequestMapping("/goodsBySkus")
    public PublicResult<List<ShopCartVO>> goodsBySkus(@RequestBody OrderAddressDTO address) {
        if (address.getAccountId() == 0 && LoginInfo.get() == null) {
            return PublicResult.error(UserErrResultEnum.NOT_LOGIN);
        }
        if (address.getAccountId() > 0 && StringUtils.isBlank(address.getCityCode())) {
            return PublicResult.error(CommonErrResult.ERR_REQUEST);
        }
        if (address.getAccountId() == 0) {
            address.setAccountId(LoginInfo.get().getAccountId());
        }
        List<ShopCartVO> shopCarts = goodsService.goodsBySkus(address);
        if (CollectionUtils.isEmpty(shopCarts)) {
            return PublicResult.error(GoodsErrResultEnum.BUY_CACHE_NOT_EXISTS);
        }
        return PublicResult.success(shopCarts);
    }

    @PostMapping("/orderGoods")
    public List<ShopCartVO> orderGoods(@RequestBody OrderAddressDTO address) {
        if (address.getAccountId() == 0 || StringUtils.isBlank(address.getCityCode())) {
            return null;
        }
        return goodsService.goodsBySkus(address);
    }

    @PostMapping("/skuOrdered/{accountId}")
    public PublicResult<?> skuOrdered(@PathVariable int accountId) {
        return goodsService.skuOrdered(accountId);
    }

    @LoginRequire
    @PutMapping("/cacheBuySkus/{skuId}/updateAmount/{amount}")
    public PublicResult<?> updateCacheBuySkusAmount(@PathVariable int skuId, @PathVariable int amount) {
        return goodsService.updateCacheBuySkusAmount(skuId, amount);
    }

    @LoginRequire
    @GetMapping("/store/{goodsId}/detail")
    public PublicResult<?> goodsDetail(@PathVariable int goodsId) {
        return PublicResult.success(goodsService.goodsDetail(goodsId));
    }

    @LoginRequire
    @PutMapping("/store/{goodsId}/withdraw")
    public PublicResult<?> withdrawGoods(@PathVariable int goodsId) {
        return PublicResult.success(goodsService.withdrawGoods(goodsId));
    }

    @LoginRequire
    @PutMapping("/store/{goodsId}/stack")
    public PublicResult<?> stackGoods(@PathVariable int goodsId) {
        return PublicResult.success(goodsService.stackGoods(goodsId));
    }

    @LoginRequire
    @PutMapping("/store/save")
    public PublicResult<?> goodsDetail(@RequestBody StoreGoodsDTO storeGoods) {
        return goodsService.saveStoreGoods(storeGoods);
    }
}
