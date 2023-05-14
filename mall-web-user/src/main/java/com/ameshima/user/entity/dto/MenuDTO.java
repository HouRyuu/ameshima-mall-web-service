package com.ameshima.user.entity.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuDTO {

    private Integer id;
    private String menu;
    private String menuUrl;
    private Integer parentMenu;
    private List<MenuDTO> childrenMenus = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Integer parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<MenuDTO> getChildrenMenus() {
        return childrenMenus;
    }

    public void setChildrenMenus(List<MenuDTO> childrenMenus) {
        this.childrenMenus = childrenMenus;
    }
}
