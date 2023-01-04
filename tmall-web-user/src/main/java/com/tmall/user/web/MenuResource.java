package com.tmall.user.web;

import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.MenuDTO;
import com.tmall.user.service.MenuService;
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
