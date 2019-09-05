package com.tmall.basicData.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmall.basicData.entity.dto.RegionDTO;
import com.tmall.basicData.mapper.RegionMapper;
import com.tmall.basicData.service.RegionService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public List<RegionDTO> findRegion(RegionDTO param) {
        return regionMapper.findRegion(param);
    }
}
