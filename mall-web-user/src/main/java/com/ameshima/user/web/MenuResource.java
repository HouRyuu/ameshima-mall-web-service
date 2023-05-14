package com.ameshima.user.web;

import com.ameshima.user.entity.dto.MenuDTO;
import com.ameshima.user.service.MenuService;
import com.ameshima.common.annotation.LoginRequire;
import com.ameshima.common.dto.PublicResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuResource {

    @Resource
    private MenuService menuService;

    @GetMapping
    @LoginRequire
    public PublicResult<List<MenuDTO>> findMenu() {
        return PublicResult.success(menuService.findMenu());
    }
}
