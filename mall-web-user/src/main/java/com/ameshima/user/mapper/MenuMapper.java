package com.ameshima.user.mapper;

import com.ameshima.user.entity.dto.MenuDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper {

    List<MenuDTO> findMenu(@Param("storeId") Integer storeId);

}
