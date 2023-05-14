package com.ameshima.common.redis;

import java.util.concurrent.TimeUnit;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface KeyPrefix {

    String prefix();

    long timeout();

    TimeUnit timeunit();

}
