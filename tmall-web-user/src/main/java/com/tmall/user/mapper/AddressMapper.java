package com.tmall.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tmall.common.BaseMapper;
import com.tmall.user.entity.dto.AddressDTO;
import com.tmall.user.entity.po.AddressPO;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
public interface AddressMapper extends BaseMapper<AddressPO> {

    List<AddressDTO> findAll(@Param("accountId") int accountId);

    int createAddr(AddressDTO address);

    int setDefault(@Param("id") int id, @Param("accountId") int accountId);
}
