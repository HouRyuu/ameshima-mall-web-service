package com.tmall.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tmall.common.BaseMapper;
import com.tmall.user.entity.dto.AddressDTO;
import com.tmall.user.entity.po.AddressPO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface AddressMapper extends BaseMapper<AddressPO> {

    List<AddressDTO> findAll(@Param("accountId") int accountId);

    int createAddr(AddressDTO address);

    int setDefault(@Param("id") int id, @Param("accountId") int accountId);
}
