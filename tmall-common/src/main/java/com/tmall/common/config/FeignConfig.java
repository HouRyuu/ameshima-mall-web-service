package com.tmall.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.dto.PublicResult;
import feign.*;
import feign.codec.Decoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Type;

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
            requestTemplate.header(TmallConstant.TOKEN, attrs.getRequest().getHeader(TmallConstant.TOKEN));
        }
    }
}
