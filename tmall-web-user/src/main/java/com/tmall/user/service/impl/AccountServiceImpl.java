package com.tmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.GlobalConfig;
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
import com.tmall.user.keys.UserKey;
import com.tmall.user.mapper.AccountMapper;
import com.tmall.user.service.AccountService;
import com.tmall.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Objects;

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

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RedisClient redisClient;
    @Resource
    private UserService userService;
    @Resource
    private GlobalConfig globalConfig;

    @Override
    public int create(LoginUser account) {
        Assert.notNull(account, "account can not be null.");
        if (StringUtils.isBlank(account.getAccount())) {
            account.setAccount(CommonUtil.getUuid());
        }
        if (StringUtils.isNotBlank(account.getPassword())) {
            account.setPassword(DigestUtils.md5Hex(DigestUtils.md5(account.getPassword())));
        }
        AccountPO accountPO = new AccountPO();
        accountPO.setAccount(account.getAccount());
        accountPO.setPassword(account.getPassword());
        accountPO.setFirstUserType(account.getAccountType());
        accountMapper.insertSelective(accountPO);
        return accountPO.getId();
    }

    @Override
    @Transactional
    public int createForDefaultUser(LoginUser account) {
        account.setAccountId(create(account));
        userService.createUser(account);
        return account.getAccountId();
    }

    @Override
    public PublicResult<String> login(AccountPO account) {
        Assert.isTrue(account != null && !StringUtils.isAnyBlank(account.getAccount(), account.getPassword()),
                TmallConstant.PARAM_ERR_MSG);
        account.setPassword(DigestUtils.md5Hex(DigestUtils.md5(account.getPassword())));
        LoginUser loginUser = accountMapper.login(account);
        if (loginUser == null) {
            return PublicResult.error(UserErrResultEnum.LOGIN_FAIL);
        }
        String token;
        do {
            token = CommonUtil.getUuid();
        } while (redisClient.get(CommonKey.TOKEN, token) != null);
        redisClient.set(CommonKey.TOKEN, token, loginUser);
        return PublicResult.success(token);
    }

    public PublicResult<String> register(RegisterDTO registerInfo) {
        this.checkRegisterInfo(registerInfo);
        if (!Objects.equals(registerInfo.getCaptcha(),
                redisClient.get(UserKey.CAPTCHA_REGISTER, registerInfo.getAccount()))) {
            return PublicResult.error(UserErrResultEnum.CAPTCHA_ERR);
        }
        try {
            LoginUser loginUser = new LoginUser();
            loginUser.setAccount(registerInfo.getAccount());
            loginUser.setPassword(registerInfo.getPassword());
            loginUser.setNickName(registerInfo.getNickName());

            loginUser.setAccountId(createForDefaultUser(loginUser));
            loginUser.setPassword(null);
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

    @Override
    public PublicResult<?> sendRegisterCaptcha(String account) {
        Assert.hasText(account, TmallConstant.PARAM_ERR_MSG);
        Example example = new Example(AccountPO.class);
        example.and().andEqualTo("account", account).andCondition("is_delete", TmallConstant.NO);
        if (accountMapper.selectCountByExample(example) > 0) {
            LOGGER.warn("携帯番号もう登録された=>{}", account);
            return PublicResult.error(UserErrResultEnum.REG_ACCOUNT_EXISTS);
        }
        String captcha = CommonUtil.createCaptcha();
        LOGGER.info("{}にキャプチャ=>{}を送る", account, captcha);
        redisClient.set(UserKey.CAPTCHA_REGISTER, account, captcha);
        return PublicResult.success(globalConfig.get(GlobalConfig.KEY_LIMIT_CAPTCHA));
    }

    @Override
    public PublicResult<?> sendForgetCaptcha(String account) {
        Assert.hasText(account, TmallConstant.PARAM_ERR_MSG);
        Example example = new Example(AccountPO.class);
        example.and().andEqualTo("account", account).andCondition("is_delete", TmallConstant.NO);
        if (accountMapper.selectCountByExample(example) > 0) {
            LOGGER.warn("携帯番号は登録されていない=>{}", account);
            return PublicResult.error(UserErrResultEnum.ACCOUNT_NOT_EXISTS);
        }
        String captcha = CommonUtil.createCaptcha();
        LOGGER.info("{}にキャプチャ=>{}を送る", account, captcha);
        redisClient.set(UserKey.CAPTCHA_FORGET, account, captcha);
        return PublicResult.success(globalConfig.get(GlobalConfig.KEY_LIMIT_CAPTCHA));
    }

    @Override
    public PublicResult<String> forgetPwd(RegisterDTO account) {
        this.checkAccountInfo(account);
        if (!Objects.equals(account.getCaptcha(), redisClient.get(UserKey.CAPTCHA_FORGET, account.getAccount()))) {
            return PublicResult.error(UserErrResultEnum.CAPTCHA_ERR);
        }
        AccountPO record = new AccountPO();
        record.setPassword(DigestUtils.md5Hex(DigestUtils.md5(account.getPassword())));
        Example example = new Example(AccountPO.class);
        example.and().andEqualTo("account", account.getAccount()).andCondition("is_delete=0");
        if (accountMapper.updateByExampleSelective(record, example) < 1) {
            return PublicResult.error(UserErrResultEnum.ACCOUNT_NOT_EXISTS);
        }
        record.setAccount(account.getAccount());
        record.setPassword(account.getPassword());
        return login(record);
    }

    private void checkAccountInfo(RegisterDTO account) {
        Assert.isTrue(
                account != null
                        && !StringUtils.isAnyBlank(account.getAccount(), account.getPassword(), account.getCaptcha()),
                TmallConstant.PARAM_ERR_MSG);
        CheckUtil.checkMobile(account.getAccount());
        CheckUtil.checkStrLength(account.getPassword(), 6, 32);
        CheckUtil.checkStrLength(account.getCaptcha(), 6, 6);
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
