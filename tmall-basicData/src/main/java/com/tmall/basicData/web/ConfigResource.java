package com.tmall.basicData.web;

import javax.annotation.Resource;

import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.utils.FileUtil;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.dto.PublicResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public PublicResult<?> getValue(@PathVariable String category, @PathVariable String name) {
        return PublicResult.success(globalConfig.get(category + GlobalConfig.CONNECTOR + name));
    }

    @PostMapping("/img/upload")
    public PublicResult<String> imgUpload(@RequestParam("img") MultipartFile avatarFile) throws IOException {
        return PublicResult.success(FileUtil.compressImgToBase64(avatarFile.getInputStream()));
    }

}
