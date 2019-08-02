package com.tmall.common.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.tmall.common.constants.AlipayAttr;
import com.tmall.common.constants.TmallConstant;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
public class AlipayUtil {

    @Autowired
    private AlipayAttr alipayAttr;
    public AlipayClient alipayClient;

    @PostConstruct
    public void init() {
        initAlipayClient();
    }

    private void initAlipayClient() {
        this.alipayClient = new DefaultAlipayClient(alipayAttr.getGateway(), // 支付宝网关（固定）
                alipayAttr.getAppId(), // APPID 即创建应用后生成
                alipayAttr.getAppPrivateKey(), // 开发者私钥，由开发者自己生成
                TmallConstant.JSON_STR, // 参数返回格式，只支持json
                TmallConstant.UTF8_STR, // 编码集，支持GBK/UTF-8
                alipayAttr.getPublicKey(), // 支付宝公钥，由支付宝生成
                alipayAttr.getSignType()); // 商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
    }

}
