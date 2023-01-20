package com.tmall.store.service;

import java.util.List;

import com.tmall.store.entity.dto.StoreBrandDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreBrandService {

    List<StoreBrandDTO> findIndexBrands();

}
