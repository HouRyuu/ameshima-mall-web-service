package com.tmall.user.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.tmall.common.constants.AlipayErrResultEnum;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.common.utils.CommonUtil;
import com.tmall.user.service.UserAlipayService;

import javax.annotation.Resource;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@RestController
@RequestMapping("/alipay")
public class AlipayResource {

    @Resource
    private UserAlipayService userAlipayService;
    @Resource
    private RedisClient redisClient;

    @GetMapping("/auth")
    public PublicResult<?>  alipayBusinessLogin(@RequestParam("auth_code") String authCode) throws AlipayApiException {
        // String appId = request.getParameter("app_id");
        // String source = request.getParameter("source");
        // String appAuthCode = request.getParameter("auth_code");
        // String uuid = request.getParameter("auth_code");
        // AlipaySystemOauthTokenRequest authRequest = new AlipaySystemOauthTokenRequest();
        // authRequest.setCode(appAuthCode);
        // authRequest.setGrantType("authorization_code");
        // // 发送请求得到响应
        // AlipaySystemOauthTokenResponse oauthTokenResponse = alipayUtil.alipayClient.execute(authRequest);
        // System.out.println(oauthTokenResponse.getAccessToken());
        if (StringUtils.isBlank(authCode)) {
            return null;
        }
        LoginUser loginInfo = userAlipayService.login(authCode);
        if (loginInfo == null) {
            return PublicResult.error(AlipayErrResultEnum.AUTH_FAIL);
        }
        String token;
        do {
            token = CommonUtil.getUuid();
        } while (redisClient.get(CommonKey.TOKEN, token) != null);
        redisClient.set(CommonKey.TOKEN, token, loginInfo);
        return PublicResult.success(token);
    }
}
