package com.ameshima.store.service;

import com.ameshima.common.dto.PublicResult;
import com.ameshima.store.entity.dto.StoreBannerDTO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface StoreService {

    PublicResult<Integer> register(String storeName);

}
