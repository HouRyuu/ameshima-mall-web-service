package com.tmall.common.config;

import com.tmall.common.constants.MallConstant;
import feign.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignConfig implements /*Decoder,*/ RequestInterceptor {

    /*@Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        if (response.body() == null) {
            return null;
        }
        return parse(Util.toString(response.body().asReader()));
    }

    private <T> PublicResult<T> parse(String resStr) {
        return JSON.parseObject(resStr, new TypeReference<PublicResult<T>>() {
        }.getType());
    }*/

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            requestTemplate.header(MallConstant.TOKEN, attrs.getRequest().getHeader(MallConstant.TOKEN));
        }
    }
}
