package com.ameshima.goods.web;

import com.ameshima.goods.entity.dto.GoodsAttrMapDTO;
import com.ameshima.goods.service.GoodsAttrService;
import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.PublicResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attr")
public class GoodsAttrResource {

    @Resource
    private GoodsAttrService goodsAttrService;

    @GetMapping("/list")
    public PublicResult<List<GoodsAttrMapDTO>> attrList() {
        return PublicResult.success(goodsAttrService.findAttrList());
    }

    @LoginRequire
    @GetMapping("/map/{goodsId}/list")
    public PublicResult<List<GoodsAttrMapDTO>> attrMapList(@PathVariable("goodsId") int goodsId) {
        return PublicResult.success(goodsAttrService.findAttrMapList(goodsId));
    }

    @LoginRequire
    @GetMapping("/map/{goodsId}")
    public PublicResult<List<Map<String, Object>>> findGoodsAttrList(@PathVariable("goodsId") int goodsId) {
        return PublicResult.success(goodsAttrService.findGoodsAttrList(goodsId));
    }

    @LoginRequire
    @PutMapping("/map/save")
    public PublicResult<Integer> attrMapSave(@RequestBody GoodsAttrMapDTO attrMap) {
        return goodsAttrService.saveAttrMap(attrMap);
    }

    @LoginRequire
    @DeleteMapping("{goodsId}/map/{id}/delete")
    public PublicResult<?> attrMapSave(@PathVariable("goodsId") int goodsId, @PathVariable("id") int id) {
        return goodsAttrService.deleteAttrMap(id, goodsId);
    }
}
