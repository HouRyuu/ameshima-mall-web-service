package com.tmall.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmall.common.constants.TmallConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.tmall.common.annotation.LoginRequire;
import com.tmall.common.constants.UserErrResultEnum;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.common.utils.WebUtil;

/**
 * 〈機能記述〉<br>
 * 〈詳細な記述〉
 *
 * @author liupeng
 * @see [関するクラス/メソッド]（オプショナル）
 * @since [プロダクト/モジュールバージョン] （オプショナル）
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

    @Resource
    private RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ip = WebUtil.getIpAddress(request);
        StringBuffer url = request.getRequestURL();
        String token = request.getHeader(TmallConstant.TOKEN);
        LOGGER.info("ip->{}，url：{}->{}，token->{}", ip, request.getMethod(), url, token);
        LoginUser loginUser = null;
        if (StringUtils.isNotBlank(token)) {
            loginUser = redisClient.get(CommonKey.TOKEN, token);
            if (loginUser != null) {
                // 将登录信息存入本地线程供业务使用并更新cookie及redis过期时间
                LoginInfo.putToken(token);
                LoginInfo.put(loginUser);
                redisClient.setNoLog(CommonKey.TOKEN, token, loginUser);
            }
        }
        if (handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(LoginRequire.class) == null) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(PublicResult.error(UserErrResultEnum.NOT_LOGIN)));
            return false;
        }
        if (loginUser == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(PublicResult.error(UserErrResultEnum.INVALID_LOGIN)));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
        // 响应完成后清理本息线程登录信息释放内存
        if (StringUtils.isNotBlank(LoginInfo.getToken())) {
            LoginInfo.removeToken();
            LoginInfo.remove();
        }
    }
}
