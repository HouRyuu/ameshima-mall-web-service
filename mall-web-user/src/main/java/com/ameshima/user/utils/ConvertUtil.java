package com.ameshima.user.utils;

import com.ameshima.user.entity.dto.MenuDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ConvertUtil {

    public static List<MenuDTO> convertMenu(List<MenuDTO> menuList) {
        List<MenuDTO> result = Lists.newArrayList();
        Map<Integer, MenuDTO> menuMap = Maps.newHashMap();
        for (MenuDTO menu : menuList) {
            if (menu.getParentMenu() == 0) {
                result.add(menu);
                menuMap.put(menu.getId(), menu);
            } else {
                menuMap.get(menu.getParentMenu()).getChildrenMenus().add(menu);
            }
        }
        return result;
    }

}
