package com.tmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.tmall.common.constants.MallConstant;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.utils.AlipayUtil;
import com.tmall.user.entity.po.UserAlipayPO;
import com.tmall.user.mapper.UserAlipayMapper;
import com.tmall.user.service.AccountService;
import com.tmall.user.service.UserAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
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
        UserAlipayPO userAlipayPO = new UserAlipayPO();
        userAlipayPO.setUserId(userinfoShareResponse.getUserId());
        userAlipayPO = userAlipayMapper.selectOne(userAlipayPO);
        if (userAlipayPO == null) {
            userAlipayPO = new UserAlipayPO();
        }
        final UserAlipayPO userAlipay = userAlipayPO;
        return transactionTemplate.execute((status) -> {
            LoginUser loginUser = new LoginUser();
            loginUser.setAccountType(MallConstant.ACCOUNT_TYPE_ALIPAY);
            loginUser.setAvatar(userinfoShareResponse.getAvatar());
            loginUser.setNickName(userinfoShareResponse.getNickName());
            loginUser.setGender(userinfoShareResponse.getGender());
            if (userAlipay.getId() == null) {
                userAlipay.setAccountId(accountService.create(loginUser));
            }
            BeanUtils.copyProperties(userinfoShareResponse, userAlipay);
            userAlipayMapper.saveOrUpdate(userAlipay);
            return loginUser;
        });
    }

}
