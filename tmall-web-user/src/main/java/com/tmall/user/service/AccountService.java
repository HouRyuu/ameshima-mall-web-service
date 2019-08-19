package com.tmall.user.service;

import com.tmall.common.dto.AjaxResult;
import com.tmall.user.entity.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface AccountService {

    Integer create(AccountPO account);

    AjaxResult login(AccountPO account);

    /**
     * 发送注册验证码
     * 
     * @param account 账号
     * @return 发送间隔秒
     */
    AjaxResult sendRegisterCaptcha(String account);

}
