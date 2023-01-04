package com.tmall.user.service.impl;

import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.LoginUser;
import com.tmall.user.entity.dto.MenuDTO;
import com.tmall.user.mapper.MenuMapper;
import com.tmall.user.service.MenuService;
import com.tmall.user.utils.ConvertUtil;
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
