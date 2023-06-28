package com.ameshima.user.web;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.user.entity.dto.StoreUserDTO;
import com.ameshima.user.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/store")
public class StoreResource {
    @Resource
    private UserService userService;

    @PutMapping("register")
    public PublicResult<?> register(@RequestBody StoreUserDTO storeUser) {
        return userService.registerStore(storeUser);
    }
}
