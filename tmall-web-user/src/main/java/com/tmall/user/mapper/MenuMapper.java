package com.tmall.user.mapper;

import com.tmall.user.entity.dto.MenuDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper {

    List<MenuDTO> findMenu(@Param("storeId") Integer storeId);

}
