package com.tmall.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.utils.AlipayUtil;
import com.tmall.common.dto.LoginUser;
import com.tmall.user.entity.po.AccountPO;
import com.tmall.user.entity.po.UserAlipayPO;
import com.tmall.user.mapper.UserAlipayMapper;
import com.tmall.user.service.AccountService;
import com.tmall.user.service.UserAlipayService;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserAlipayMapper userAlipayMapper;
    @Autowired
    private AlipayUtil alipayUtil;
    @Autowired
    private AccountService accountService;

    @Override
    @Transactional
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
                    UserAlipayPO userAlipay = new UserAlipayPO();
                    userAlipay.setUserId(userinfoShareResponse.getUserId());
                    userAlipay = userAlipayMapper.selectOne(userAlipay);
                    if (userAlipay == null) {
                        AccountPO account = new AccountPO();
                        account.setFirstUserType(TmallConstant.ACCOUNT_TYPE_ALIPAY);
                        accountService.create(account);
                        userAlipay = new UserAlipayPO();
                        userAlipay.setAccountId(account.getId());
                    }
                    BeanUtils.copyProperties(userinfoShareResponse, userAlipay);
                    userAlipayMapper.saveOrUpdate(userAlipay);
                    LoginUser loginInfo = new LoginUser();
                    BeanUtils.copyProperties(userAlipay, loginInfo);
                    loginInfo.setAccountType(TmallConstant.ACCOUNT_TYPE_ALIPAY);
                    return loginInfo;
                }
                throw new RuntimeException(userinfoShareResponse.getSubMsg());
            }
            throw new RuntimeException(oauthTokenResponse.getSubMsg());
        } catch (Exception e) {
            LOGGER.error("支付宝用户信息保存异常", e);
        }
        return null;
    }
}
