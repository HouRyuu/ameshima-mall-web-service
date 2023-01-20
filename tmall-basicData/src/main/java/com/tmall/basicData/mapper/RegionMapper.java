package com.tmall.basicData.mapper;

import java.util.List;

import com.tmall.basicData.entity.dto.RegionDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface RegionMapper {

    List<RegionDTO> findRegion(RegionDTO param);

}
