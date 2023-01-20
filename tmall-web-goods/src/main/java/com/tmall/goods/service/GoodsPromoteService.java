package com.tmall.goods.service;

import com.tmall.goods.entity.dto.GoodsPromoteDTO;

import java.util.List;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface GoodsPromoteService {

    List<GoodsPromoteDTO> findPromotes();

}
