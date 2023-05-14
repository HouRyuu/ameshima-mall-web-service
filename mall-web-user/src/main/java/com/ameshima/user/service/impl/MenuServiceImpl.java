package com.ameshima.user.service.impl;

import com.ameshima.common.dto.LoginInfo;
import com.ameshima.user.entity.dto.MenuDTO;
import com.ameshima.user.mapper.MenuMapper;
import com.ameshima.user.service.MenuService;
import com.ameshima.user.utils.ConvertUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuDTO> findMenu() {
        return ConvertUtil.convertMenu(menuMapper.findMenu(LoginInfo.get().getStoreId()));
    }
}
