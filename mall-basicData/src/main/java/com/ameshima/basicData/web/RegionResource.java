package com.ameshima.basicData.web;

import com.ameshima.basicData.service.RegionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ameshima.basicData.entity.dto.RegionDTO;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.dto.PublicResult;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
@RequestMapping("/region")
public class RegionResource {

    @Resource
    private RegionService regionService;

    @GetMapping("/provinces")
    private PublicResult<?>  provinces() {
        return PublicResult.success(regionService.findRegion(new RegionDTO(MallConstant.REGION_LEVEL_PROVINCE)));
    }

    @GetMapping("/{parentCode}/findByParent")
    private PublicResult<?>  findByParent(@PathVariable String parentCode) {
        return PublicResult.success(regionService.findRegion(new RegionDTO(parentCode)));
    }

}
