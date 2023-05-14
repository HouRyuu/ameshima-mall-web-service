package com.ameshima.store.mapper;

import java.util.List;

import com.ameshima.store.entity.dto.StoreBrandDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreBrandMapper {

    int getStoreBrandCount(StoreBrandDTO queryParam);

    List<StoreBrandDTO> findStoreBrands(StoreBrandDTO queryParam);

}
