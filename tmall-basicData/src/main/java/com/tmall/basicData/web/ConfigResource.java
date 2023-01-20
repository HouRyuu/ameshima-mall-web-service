package com.tmall.basicData.web;

import javax.annotation.Resource;

import com.tmall.common.utils.FileUtil;
import org.springframework.web.bind.annotation.*;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.dto.PublicResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
    public PublicResult<String> imgUpload(@RequestParam("img") MultipartFile file) throws IOException {
        return PublicResult.success(FileUtil.compressImgToBase64(file.getInputStream(), file.getContentType()));
    }

}
