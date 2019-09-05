package com.tmall.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.tmall.common.dto.AjaxResult;
import com.tmall.common.dto.LoginInfo;
import com.tmall.common.dto.LoginUser;
import com.tmall.common.redis.RedisClient;
import com.tmall.common.redis.key.CommonKey;
import com.tmall.common.utils.WebUtil;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
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
        String token = request.getHeader("token");
        LOGGER.info("ip->{}，请求地址：{}->{}，token->{}", ip, request.getMethod(), url, token);
        LoginUser loginUser = null;
        if (StringUtils.isNotBlank(token)) {
            loginUser = redisClient.get(CommonKey.TOKEN, token);
            if (loginUser != null) {
                // 将登录信息存入本地线程供业务使用并更新cookie及redis过期时间
                LoginInfo.putToken(token);
                LoginInfo.put(loginUser);
                redisClient.set(CommonKey.TOKEN, token, loginUser);
            }
        }
        if (handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(LoginRequire.class) == null) {
            return true;
        }
        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(AjaxResult.error(UserErrResultEnum.NOT_LOGIN)));
            return false;
        }
        if (loginUser == null) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSON(AjaxResult.error(UserErrResultEnum.INVALID_LOGIN)));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Object o, Exception e) throws Exception {
        // 响应完成后清理本息线程登录信息释放内存
        if (StringUtils.isNotBlank(LoginInfo.getToken())) {
            LoginInfo.removeToken();
            LoginInfo.remove();
        }
    }
}