package com.tmall.user.mapper;

import com.tmall.common.BaseMapper;
import com.tmall.common.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface AccountMapper extends BaseMapper<AccountPO> {

    LoginUser login(AccountPO account);

}
