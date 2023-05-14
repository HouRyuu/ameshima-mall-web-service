package com.ameshima.user.service;

import com.ameshima.user.entity.dto.MenuDTO;

import java.util.List;

public interface MenuService {

    List<MenuDTO> findMenu();

}
