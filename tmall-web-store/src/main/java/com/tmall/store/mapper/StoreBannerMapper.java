package com.tmall.store.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tmall.store.entity.dto.StoreBannerDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreBannerMapper {

    int getBannerCount(@Param("isIndexShow") Integer isIndexShow);

    List<StoreBannerDTO> findBanners(StoreBannerDTO queryParam);

}
