package com.tmall.user.service;

import com.tmall.common.dto.LoginUser;
import com.tmall.common.dto.PublicResult;
import com.tmall.user.entity.dto.RegisterDTO;
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

    int create(LoginUser account);

    int createForDefaultUser(LoginUser account);

    PublicResult<String> login(AccountPO account);

    PublicResult<String> register(RegisterDTO registerInfo);

    /**
     * 登録キャプチャを送信
     *
     * @param account 　アカウント
     * @return 次回送れる時間（秒）
     */
    PublicResult<?> sendRegisterCaptcha(String account);

    /**
     * パスワードが忘れたキャプチャを送信
     *
     * @param account アカウント
     * @return 次回送れる時間（秒）
     */
    PublicResult<?> sendForgetCaptcha(String account);

    PublicResult<String> forgetPwd(RegisterDTO account);

}