package com.ameshima.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.ameshima.user.utils.CaptchaSender;
import com.ameshima.common.constants.GlobalConfig;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.constants.UserErrResultEnum;
import com.ameshima.common.dto.LoginUser;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.common.redis.key.CommonKey;
import com.ameshima.common.utils.CheckUtil;
import com.ameshima.common.utils.CommonUtil;
import com.ameshima.user.entity.dto.RegisterDTO;
import com.ameshima.user.entity.po.AccountPO;
import com.ameshima.user.keys.UserKey;
import com.ameshima.user.mapper.AccountMapper;
import com.ameshima.user.service.AccountService;
import com.ameshima.user.service.UserService;
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
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
    @Resource
    private CaptchaSender captchaSender;

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
                MallConstant.PARAM_ERR_MSG);
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
        Assert.hasText(account, MallConstant.PARAM_ERR_MSG);
        Example example = new Example(AccountPO.class);
        example.and().andEqualTo("account", account).andCondition("is_delete", MallConstant.NO);
        if (accountMapper.selectCountByExample(example) > 0) {
            LOGGER.warn("メールアドレスもう登録された=>{}", account);
            return PublicResult.error(UserErrResultEnum.REG_ACCOUNT_EXISTS);
        }
        captchaSender.sendCaptchaMail(account);
        /*String captcha = CommonUtil.createCaptcha();
        LOGGER.info("{}にキャプチャ=>{}を送る", account, captcha);
        redisClient.set(UserKey.CAPTCHA_REGISTER, account, captcha);*/
        return PublicResult.success(globalConfig.get(GlobalConfig.KEY_LIMIT_CAPTCHA));
    }

    @Override
    public PublicResult<?> sendForgetCaptcha(String account) {
        Assert.hasText(account, MallConstant.PARAM_ERR_MSG);
        Example example = new Example(AccountPO.class);
        example.and().andEqualTo("account", account).andEqualTo("isDelete", MallConstant.NO);
        if (accountMapper.selectCountByExample(example) == 0) {
            LOGGER.warn("メールアドレスは登録されていない=>{}", account);
            return PublicResult.error(UserErrResultEnum.ACCOUNT_NOT_EXISTS);
        }
        captchaSender.sendCaptchaMail(account);
        /*String captcha = CommonUtil.createCaptcha();
        LOGGER.info("{}にキャプチャ=>{}を送る", account, captcha);
        redisClient.set(UserKey.CAPTCHA_FORGET, account, captcha);*/
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
                MallConstant.PARAM_ERR_MSG);
        CheckUtil.checkMobile(account.getAccount());
        CheckUtil.checkStrLength(account.getPassword(), 6, 32);
        CheckUtil.checkStrLength(account.getCaptcha(), 6, 6);
    }

    private void checkRegisterInfo(RegisterDTO registerInfo) {
        Assert.isTrue(registerInfo != null && !StringUtils.isAnyBlank(registerInfo.getAccount(),
                        registerInfo.getPassword(), registerInfo.getNickName(), registerInfo.getCaptcha()),
                MallConstant.PARAM_ERR_MSG);
        CheckUtil.checkMobile(registerInfo.getAccount());
        CheckUtil.checkStrLength(registerInfo.getPassword(), 6, 32);
        CheckUtil.checkStrLength(registerInfo.getNickName(), 1, 16);
        CheckUtil.checkStrLength(registerInfo.getCaptcha(), 6, 6);
    }

}
