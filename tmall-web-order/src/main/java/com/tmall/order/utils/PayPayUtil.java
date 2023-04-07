package com.tmall.order.utils;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.dto.PublicResult;
import com.tmall.common.redis.RedisClient;
import com.tmall.order.keys.OrderKey;
import jp.ne.paypay.ApiClient;
import jp.ne.paypay.ApiException;
import jp.ne.paypay.Configuration;
import jp.ne.paypay.api.PaymentApi;
import jp.ne.paypay.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

@Component
@ConfigurationProperties("paypay")
public class PayPayUtil {

    private static final String SUCCESS = "SUCCESS";
    private static final Logger LOGGER = LoggerFactory.getLogger(PayPayUtil.class);

    @Resource
    private RedisClient redisClient;

    private String apiKey;
    private String secret;
    private String assumeMerchant;
    private boolean productionMode;
    private String codeType;
    private static volatile ApiClient apiClient;

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAssumeMerchant(String assumeMerchant) {
        this.assumeMerchant = assumeMerchant;
    }

    public void setProductionMode(boolean productionMode) {
        this.productionMode = productionMode;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }


    public static class OrderInfo implements Serializable {
        private final String paymentId;
        private final int amount;
        private final String orderDesc;

        public OrderInfo(String paymentId, int amount, String orderDesc) {
            this.paymentId = paymentId;
            this.amount = amount;
            this.orderDesc = orderDesc;
        }

        @Override
        public String toString() {
            return "OrderInfo{" +
                    "paymentId='" + paymentId + '\'' +
                    ", amount=" + amount +
                    ", orderDesc='" + orderDesc + '\'' +
                    '}';
        }
    }

    public PublicResult<String> createQRCode(OrderInfo orderInfo) {
        QRCode qrCode = new QRCode();
        qrCode.setAmount(new MoneyAmount().amount(orderInfo.amount).currency(MoneyAmount.CurrencyEnum.JPY));
        qrCode.setMerchantPaymentId(orderInfo.paymentId);
        qrCode.setCodeType(codeType);
        qrCode.setOrderDescription(orderInfo.orderDesc);
        qrCode.isAuthorization(false);
        LOGGER.info("{}でPayPayのQRCodeをもらっている", orderInfo);
        try {
            PaymentApi apiInstance = new PaymentApi(getApiClient());
            QRCodeDetails response = apiInstance.createQRCode(qrCode);
            ResultInfo resultInfo = response.getResultInfo();
            LOGGER.info("{}でもらったPayPayのQRCode=>{}", orderInfo, JSON.toJSONString(response));
            if (SUCCESS.equals(resultInfo.getCode())) {
                redisClient.set(OrderKey.PAYPAY_CODE_ID, orderInfo.paymentId, response.getData().getCodeId());
                return PublicResult.success(response.getData().getUrl());
            }
            return PublicResult.error(Integer.parseInt(resultInfo.getCode()), resultInfo.getMessage());
        } catch (ApiException e) {
            LOGGER.error(String.format("Paypay支払い%1$s=>QRコード生成がエラーになった", orderInfo), e);
            return PublicResult.error(CommonErrResult.OPERATE_FAIL.errCode(), e.getResponseBody());
        }
    }

    public PublicResult<String> deleteQRCode(String paymentId) {
        String codeId = redisClient.get(OrderKey.PAYPAY_CODE_ID, paymentId);
        if (StringUtils.isBlank(codeId)) {
            LOGGER.warn("paymentId=>{}にはQRCodeIDがない", paymentId);
            return PublicResult.success();
        }
        try {
            LOGGER.warn("paymentId=>{}のQRCodeID=>{}を削除し始める", paymentId, codeId);
            PaymentApi apiInstance = new PaymentApi(getApiClient());
            NotDataResponse response = apiInstance.deleteQRCode(codeId);
            LOGGER.info("paymentId=>{}のQRCodeID=>{}削除response=>{}", paymentId, codeId, JSON.toJSONString(response));
            if (SUCCESS.equals(response.getResultInfo().getCode())) {
                redisClient.removeKey(OrderKey.PAYPAY_CODE_ID, paymentId);
                return PublicResult.success();
            }
        } catch (ApiException e) {
            LOGGER.error(String.format("paymentId=>%1$sのQRCodeID=>%2$s削除がエラーになった", paymentId, codeId), e);
        }
        return PublicResult.error();
    }

    public PublicResult<String> getPayDetail(String merchantPaymentId) {
        try {
            // Calling the method to get payment details
            PaymentApi apiInstance = new PaymentApi(getApiClient());
            PaymentDetails response = apiInstance.getCodesPaymentDetails(merchantPaymentId);
            ResultInfo resultInfo = response.getResultInfo();
            LOGGER.info("{}のPayPay支払う状態=>{}", merchantPaymentId, JSON.toJSONString(response));
            // Printing if the method call was SUCCESS, this does not mean the payment was a success
            if (SUCCESS.equals(resultInfo.getCode())) {
                String status = response.getData().getStatus().getValue();
                PublicResult<String> result = PublicResult.success(status);
                if (PaymentState.StatusEnum.COMPLETED.getValue().equals(status)) {
                    // PublicResult.errMsgでpaypayのPaymentIdを暫く記憶する
                    result.setErrMsg(response.getData().getPaymentId());
                }
                return result;
            }
            return PublicResult.error(CommonErrResult.OPERATE_FAIL.errCode(), resultInfo.getMessage());
        } catch (ApiException e) {
            LOGGER.error(String.format("Paypay%1$s=>支払う状態の取得がエラーになった", merchantPaymentId), e);
            return PublicResult.error();
        }
    }

    private ApiClient getApiClient() throws ApiException {
        if (apiClient == null) {
            synchronized (ApiClient.class) {
                if (apiClient == null) {
                    apiClient = new Configuration().getDefaultApiClient();
                    apiClient.setProductionMode(productionMode);
                    apiClient.setApiKey(apiKey);
                    apiClient.setApiSecretKey(secret);
                    apiClient.setAssumeMerchant(assumeMerchant);
                }
            }
        }
        return apiClient;
    }

}
