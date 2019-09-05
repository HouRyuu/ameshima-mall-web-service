package com.tmall.basicData.mapper;

import java.util.List;

import com.tmall.basicData.entity.dto.RegionDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface RegionMapper {

    List<RegionDTO> findRegion(RegionDTO param);

}
