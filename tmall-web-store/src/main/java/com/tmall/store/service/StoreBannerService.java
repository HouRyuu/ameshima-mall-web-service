package com.tmall.store.service;

import com.tmall.store.entity.dto.StoreBannerDTO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreBannerService {

    List<StoreBannerDTO> findIndexBanner();

}
