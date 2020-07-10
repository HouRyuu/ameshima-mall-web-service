package com.tmall.user.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.utils.AlipayUtil;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.entity.po.UserAlipayPO;
import com.tmall.user.mapper.UserAlipayMapper;
import com.tmall.user.service.AccountService;
import com.tmall.user.service.UserAlipayService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class UserAlipayServiceImpl implements UserAlipayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAlipayServiceImpl.class);

    @Resource
    private UserAlipayMapper userAlipayMapper;
    @Resource
    private AlipayUtil alipayUtil;
    @Resource
    private AccountService accountService;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public LoginUser login(String authCode) {
        LOGGER.info("支付宝返回authCode=>{}", authCode);
        AlipaySystemOauthTokenRequest authRequest = new AlipaySystemOauthTokenRequest();
        authRequest.setCode(authCode);
        authRequest.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayUtil.alipayClient.execute(authRequest);
            LOGGER.info("获取支付宝访问令牌=>{}", JSON.toJSONString(oauthTokenResponse));
            if (oauthTokenResponse.isSuccess()) {
                AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
                AlipayUserInfoShareResponse userinfoShareResponse = alipayUtil.alipayClient.execute(request,
                        oauthTokenResponse.getAccessToken());
                LOGGER.info("获取支付宝用户信息=>{}", JSON.toJSONString(userinfoShareResponse));
                if (userinfoShareResponse.isSuccess()) {
                    return getLoginInfo(userinfoShareResponse);
                }
                throw new RuntimeException(userinfoShareResponse.getSubMsg());
            }
            throw new RuntimeException(oauthTokenResponse.getSubMsg());
        } catch (Exception e) {
            LOGGER.error("支付宝用户信息保存异常", e);
        }
        return null;
    }

    private LoginUser getLoginInfo(AlipayUserInfoShareResponse userinfoShareResponse) {
        final UserAlipayPO[] userAlipay = { new UserAlipayPO() };
        userAlipay[0].setUserId(userinfoShareResponse.getUserId());
        userAlipay[0] = userAlipayMapper.selectOne(userAlipay[0]);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                if (userAlipay[0] == null) {
                    AccountPO account = new AccountPO();
                    account.setFirstUserType(TmallConstant.ACCOUNT_TYPE_ALIPAY);
                    accountService.create(account);
                    userAlipay[0] = new UserAlipayPO();
                    userAlipay[0].setAccountId(account.getId());
                }
                BeanUtils.copyProperties(userinfoShareResponse, userAlipay[0]);
                userAlipayMapper.saveOrUpdate(userAlipay[0]);
            }
        });
        LoginUser loginInfo = new LoginUser();
        BeanUtils.copyProperties(userAlipay[0], loginInfo);
        loginInfo.setAccountType(TmallConstant.ACCOUNT_TYPE_ALIPAY);
        return loginInfo;
    }

}
