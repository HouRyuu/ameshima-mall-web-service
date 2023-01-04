package com.tmall.user.service;

import com.tmall.user.entity.dto.MenuDTO;

import java.util.List;

public interface MenuService {

    List<MenuDTO> findMenu();

}
