package com.tmall.user.service;

import java.util.List;

import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.AddressDTO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface AddressService {

    PublicResult<Integer> save(AddressDTO address, int accountId);

    PublicResult<?>  remove(int id, int accountId);

    List<AddressDTO> findList(int accountId);

    PublicResult<?>  setDefault(int id, int accountId);

}
