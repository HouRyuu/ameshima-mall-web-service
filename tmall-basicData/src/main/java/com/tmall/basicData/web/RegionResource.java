package com.tmall.basicData.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.basicData.entity.dto.RegionDTO;
import com.tmall.basicData.service.RegionService;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.PublicResult;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/region")
public class RegionResource {

    @Autowired
    private RegionService regionService;

    @GetMapping("/provinces")
    private PublicResult provinces() {
        return PublicResult.success(regionService.findRegion(new RegionDTO(TmallConstant.REGION_LEVEL_PROVINCE)));
    }

    @GetMapping("/{parentCode}/findByParent")
    private PublicResult findByParent(@PathVariable String parentCode) {
        return PublicResult.success(regionService.findRegion(new RegionDTO(parentCode)));
    }

}
