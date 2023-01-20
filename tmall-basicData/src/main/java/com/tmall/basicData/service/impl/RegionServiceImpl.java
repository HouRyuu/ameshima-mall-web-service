package com.tmall.basicData.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tmall.basicData.entity.dto.RegionDTO;
import com.tmall.basicData.mapper.RegionMapper;
import com.tmall.basicData.service.RegionService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Override
    public List<RegionDTO> findRegion(RegionDTO param) {
        return regionMapper.findRegion(param);
    }
}
