package com.ameshima.common.config;

import javax.annotation.Resource;

import com.ameshima.common.interceptor.LoginInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/***
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }

    @Bean
    public RequestInterceptor feignInterceptor() {
        return new FeignConfig();
    }

}
