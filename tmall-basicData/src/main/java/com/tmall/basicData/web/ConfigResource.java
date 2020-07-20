package com.tmall.basicData.web;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tmall.common.constants.GlobalConfig;
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
public class ConfigResource {

    @Resource
    private GlobalConfig globalConfig;

    @GetMapping("/{category}/{name}")
    public PublicResult getValue(@PathVariable String category, @PathVariable String name) {
        return PublicResult.success(globalConfig.get(category + GlobalConfig.CONNECTOR + name));
    }

}
