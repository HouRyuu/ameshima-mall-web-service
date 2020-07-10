package com.tmall.user.service.impl;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.constants.UserErrResultEnum;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.common.utils.CheckUtil;
import com.tmall.common.utils.CommonUtil;
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

    @Resource
    private UserMapper userMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private RedisClient redisClient;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public PublicResult<String> register(RegisterDTO registerInfo) {
        this.checkRegisterInfo(registerInfo);
        if (!Objects.equals(registerInfo.getCaptcha(),
                redisClient.get(UserKey.CAPTCHA_REGISTER, registerInfo.getAccount()))) {
            return PublicResult.error(UserErrResultEnum.CAPTCHA_ERR);
        }
        try {
            LoginUser loginUser = new LoginUser();
            loginUser.setAccountId(this.createAccount(registerInfo));
            loginUser.setNickName(registerInfo.getNickName());
            String token;
            do {
                token = CommonUtil.getUuid();
            } while (redisClient.get(CommonKey.TOKEN, token) != null);
            redisClient.set(CommonKey.TOKEN, token, loginUser);
            return PublicResult.success(token);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Account exists. param=>{}", JSON.toJSONString(registerInfo), e);
            return PublicResult.error(UserErrResultEnum.REG_ACCOUNT_EXISTS);
        } catch (Exception e) {
            LOGGER.error("Register exception. param=>{}", JSON.toJSONString(registerInfo), e);
            return PublicResult.error(UserErrResultEnum.REG_FAIL);
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

    private int createAccount(RegisterDTO registerInfo) {
        AccountPO account = new AccountPO();
        account.setAccount(registerInfo.getAccount());
        account.setPassword(registerInfo.getPassword());
        return transactionTemplate.execute((status) -> {
            int accountId = accountService.create(account);
            UserPO user = new UserPO();
            user.setAccountId(accountId);
            user.setNickName(registerInfo.getNickName().trim());
            userMapper.insertSelective(user);
            return accountId;
        });
    }

}
