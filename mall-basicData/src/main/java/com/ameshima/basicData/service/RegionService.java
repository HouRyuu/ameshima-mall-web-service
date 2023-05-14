package com.ameshima.basicData.service;

import com.ameshima.basicData.entity.dto.RegionDTO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface RegionService {

    List<RegionDTO> findRegion(RegionDTO param);

}
