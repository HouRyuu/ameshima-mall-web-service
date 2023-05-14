package com.ameshima.common;

import com.ameshima.common.po.BasePO;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface BaseMapper<T extends BasePO> extends Mapper<T>, MySqlMapper<T> {
}
