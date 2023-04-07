package com.tmall.order.utils;

import com.alibaba.fastjson.JSON;
import com.tmall.common.constants.CommonErrResult;
import com.tmall.common.constants.PayErrResultEnum;
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
        if (StringUtils.isNotBlank(redisClient.get(OrderKey.PAYPAY_CODE_ID, orderInfo.paymentId))) {
            // 既に生成した
            return PublicResult.errorWithEnum(PayErrResultEnum.DUPLICATE);
        }
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
                // RedisでQRCodeIdを保存し、支払い中を示す。支払完了かQRCodeが削除された時、RedisのQRCodeIdを削除
                redisClient.set(OrderKey.PAYPAY_CODE_ID, orderInfo.paymentId, response.getData().getCodeId());
                return PublicResult.success(response.getData().getUrl());
            }
            return PublicResult.error(Integer.parseInt(resultInfo.getCode()), resultInfo.getMessage());
        } catch (ApiException e) {
            String responseBody = e.getResponseBody();
            LOGGER.error(String.format("Paypay支払い%1$s=>QRコード%2$s生成がエラーになった", orderInfo, responseBody), e);
            if (responseBody.contains("DUPLICATE_DYNAMIC_QR_REQUEST")) {
                return PublicResult.errorWithEnum(PayErrResultEnum.DUPLICATE);
            }
            if (responseBody.contains("PRE_AUTH_CAPTURE_INVALID_EXPIRY_DATE")) {
                return PublicResult.errorWithEnum(PayErrResultEnum.EXPIRY);
            }
            return PublicResult.errorWithEnum(CommonErrResult.OPERATE_FAIL);
        }
    }

    public void deleteQRCode(String paymentId) {
        String codeId = redisClient.get(OrderKey.PAYPAY_CODE_ID, paymentId);
        LOGGER.warn("paymentId=>{}のQRCodeID=>{}を削除し始める", paymentId, codeId);
        if (StringUtils.isBlank(codeId)) {
            LOGGER.warn("paymentId=>{}にはQRCodeIDがないので、削除できない", paymentId);
            return;
        }
        try {
            PaymentApi apiInstance = new PaymentApi(getApiClient());
            NotDataResponse response = apiInstance.deleteQRCode(codeId);
            LOGGER.info("paymentId=>{}のQRCodeID=>{}削除response=>{}", paymentId, codeId, JSON.toJSONString(response));
            if (SUCCESS.equals(response.getResultInfo().getCode())) {
                redisClient.removeKey(OrderKey.PAYPAY_CODE_ID, paymentId);
            }
        } catch (ApiException e) {
            LOGGER.error(String.format("paymentId=>%1$sのQRCodeID=>%2$s削除がエラーになった", paymentId, codeId), e);
        }
    }

    public PublicResult<String> getPayDetail(String merchantPaymentId) {
        if (StringUtils.isBlank(redisClient.get(OrderKey.PAYPAY_CODE_ID, merchantPaymentId))) {
            return PublicResult.error();
        }
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
                    redisClient.removeKey(OrderKey.PAYPAY_CODE_ID, merchantPaymentId);
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
//        if (apiClient == null) {
//            synchronized (ApiClient.class) {
//                if (apiClient == null) {
                    ApiClient apiClient = new Configuration().getDefaultApiClient();
                    apiClient.setProductionMode(productionMode);
                    apiClient.setApiKey(apiKey);
                    apiClient.setApiSecretKey(secret);
                    apiClient.setAssumeMerchant(assumeMerchant);
//                }
//            }
//        }
        return apiClient;
    }

}
