package com.tmall.user.service;

import java.util.List;

import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.AddressDTO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface AddressService {

    PublicResult<Integer> save(AddressDTO address, int accountId);

    PublicResult<?>  remove(int id, int accountId);

    List<AddressDTO> findList(int accountId);

    PublicResult<?>  setDefault(int id, int accountId);

}
