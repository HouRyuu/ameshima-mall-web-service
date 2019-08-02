package com.tmall.user.web;

import com.tmall.common.constants.AlipayErrResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.tmall.common.dto.AjaxResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.utils.CommonUtil;
import com.tmall.user.entity.dto.LoginUser;
import com.tmall.user.keys.UserKey;
import com.tmall.user.service.UserAlipayService;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/user/alipay")
public class AlipayResource {

    @Autowired
    private UserAlipayService userAlipayService;
    @Autowired
    private RedisClient redisClient;

    @GetMapping("/auth")
    public AjaxResult alipayBusinessLogin(@RequestParam("auth_code") String authCode) throws AlipayApiException {
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
            return AjaxResult.error(AlipayErrResultEnum.AUTH_FAIL);
        }
        String token = CommonUtil.getUuid();
        redisClient.set(UserKey.TOKEN, token, loginInfo);
        return AjaxResult.success(token);
    }
}
