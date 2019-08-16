package com.tmall.user.service.impl;

import com.tmall.common.constants.UserErrResultEnum;
import com.tmall.common.dto.AjaxResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.tmall.common.constants.GlobalConfig;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.utils.CommonUtil;
import com.tmall.user.entity.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.keys.UserKey;
import com.tmall.user.mapper.AccountMapper;
import com.tmall.user.service.AccountService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private GlobalConfig globalConfig;

    @Override
    public Integer create(AccountPO account) {
        Assert.notNull(account, "account can not be null.");
        if (StringUtils.isBlank(account.getAccount())) {
            account.setAccount(CommonUtil.getUuid());
        }
        if (StringUtils.isNotBlank(account.getPassword())) {
            account.setPassword(DigestUtils.md5Hex(DigestUtils.md5(account.getPassword())));
        }
        accountMapper.insertSelective(account);
        return account.getId();
    }

    @Override
    public AjaxResult login(AccountPO account) {
        Assert.isTrue(account != null && !StringUtils.isAnyBlank(account.getAccount(), account.getPassword()),
                TmallConstant.PARAM_ERR_MSG);
        account.setPassword(DigestUtils.md5Hex(DigestUtils.md5(account.getPassword())));
        LoginUser loginUser =  accountMapper.login(account);
        if (loginUser == null) {
            return AjaxResult.error(UserErrResultEnum.LOGIN_FAIL);
        }
        String token = CommonUtil.getUuid();
        redisClient.set(UserKey.TOKEN, token, loginUser);
        return AjaxResult.success(token);
    }

    @Override
    public int sendRegisterCaptcha(String account) {
        String captcha = CommonUtil.createCaptcha();
        LOGGER.info("向{}发送注册验证码=>{}", account, captcha);
        redisClient.set(UserKey.CAPTCHA_REGISTER, account, captcha);
        return Integer.parseInt(globalConfig.get(GlobalConfig.KEY_LIMIT_CAPTCHA));
    }
}
