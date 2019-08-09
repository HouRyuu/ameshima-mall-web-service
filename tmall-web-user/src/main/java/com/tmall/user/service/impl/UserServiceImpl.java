package com.tmall.user.service.impl;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.constants.UserErrResultEnum;
import com.tmall.common.dto.AjaxResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.utils.CheckUtil;
import com.tmall.common.utils.CommonUtil;
import com.tmall.user.entity.dto.LoginUser;
import com.tmall.user.entity.dto.RegisterDTO;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.entity.po.UserPO;
import com.tmall.user.keys.UserKey;
import com.tmall.user.mapper.UserMapper;
import com.tmall.user.service.AccountService;
import com.tmall.user.service.UserService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedisClient redisClient;

    @Override
    @Transactional
    public AjaxResult register(RegisterDTO registerInfo) {
        this.checkRegisterInfo(registerInfo);
        if (!Objects.equals(registerInfo.getCaptcha(),
                redisClient.get(UserKey.CAPTCHA_REGISTER, registerInfo.getAccount()))) {
            return AjaxResult.error(UserErrResultEnum.CAPTCHA_ERR);
        }
        AccountPO account = new AccountPO();
        account.setAccount(registerInfo.getAccount());
        account.setPassword(registerInfo.getPassword());
        try {
            Integer accountId = accountService.create(account);
            UserPO user = new UserPO();
            user.setAccountId(accountId);
            user.setNickName(registerInfo.getNickName().trim());
            userMapper.insertSelective(user);
            LoginUser loginUser = new LoginUser();
            loginUser.setAccountId(accountId);
            loginUser.setNickName(registerInfo.getNickName());
            String token = CommonUtil.getUuid();
            redisClient.set(UserKey.TOKEN, token, loginUser);
            return AjaxResult.success(token);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Account exists. param=>{}", JSON.toJSONString(registerInfo), e);
            return AjaxResult.error(UserErrResultEnum.REG_ACCOUNT_EXISTS);
        } catch (Exception e) {
            LOGGER.error("Register exception. param=>{}", JSON.toJSONString(registerInfo), e);
            return AjaxResult.error(UserErrResultEnum.REG_FAIL);
        }
    }

    private void checkRegisterInfo(RegisterDTO registerInfo) {
        Assert.isTrue(registerInfo != null && !StringUtils.isAnyBlank(registerInfo.getAccount(),
                registerInfo.getPassword(), registerInfo.getNickName(), registerInfo.getCaptcha()),
                TmallConstant.PARAM_ERR_MSG);
        CheckUtil.checkMobile(registerInfo.getAccount());
        CheckUtil.checkStrLength(registerInfo.getPassword(), 6, 32);
        CheckUtil.checkStrLength(registerInfo.getNickName(), 1, 16);
        CheckUtil.checkStrLength(registerInfo.getCaptcha(), 6, 6);
    }

}
