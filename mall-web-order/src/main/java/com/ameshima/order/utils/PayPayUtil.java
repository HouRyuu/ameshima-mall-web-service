package com.ameshima.order.utils;

import com.alibaba.fastjson.JSON;
import com.ameshima.order.keys.OrderKey;
import com.ameshima.common.constants.CommonErrResult;
import com.ameshima.common.constants.PayErrResultEnum;
import com.ameshima.common.dto.PublicResult;
import com.ameshima.common.redis.RedisClient;
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

    public PublicResult<QRCodeResponse> createQRCode(OrderInfo orderInfo) {
        PublicResult<QRCodeResponse> result = PublicResult.success();
        result.setData(redisClient.get(OrderKey.PAYPAY_CODE, orderInfo.paymentId, () -> {
            try {
                QRCode qrCode = new QRCode();
                qrCode.setAmount(new MoneyAmount().amount(orderInfo.amount).currency(MoneyAmount.CurrencyEnum.JPY));
                qrCode.setMerchantPaymentId(orderInfo.paymentId);
                qrCode.setCodeType(codeType);
                qrCode.setOrderDescription(orderInfo.orderDesc);
                qrCode.isAuthorization(false);
                LOGGER.info("{}でPayPayのQRCodeをもらっている", orderInfo);
                QRCodeDetails response = new PaymentApi(getApiClient()).createQRCode(qrCode);
                ResultInfo resultInfo = response.getResultInfo();
                LOGGER.info("{}でもらったPayPayのQRCode=>{}", orderInfo, JSON.toJSONString(response));
                if (SUCCESS.equals(resultInfo.getCode())) {
                    redisClient.set(OrderKey.PAYPAY_CODE_ID, orderInfo.paymentId, response.getData().getCodeId());
                    return response.getData();
                }
                result.setErrCode(CommonErrResult.OPERATE_FAIL.errCode());
                result.setErrMsg(resultInfo.getMessage());
            } catch (ApiException e) {
                String responseBody = e.getResponseBody();
                LOGGER.error(String.format("PayPay支払い%1$s=>QRコード%2$s生成がエラーになった", orderInfo, responseBody), e);
                result.setErrResult(
                        responseBody.contains("DUPLICATE") ? PayErrResultEnum.DUPLICATE
                                : responseBody.contains("EXPIRY") ? PayErrResultEnum.EXPIRY
                                : CommonErrResult.OPERATE_FAIL);
            } catch (Exception e) {
                result.setErrResult(CommonErrResult.OPERATE_FAIL);
            }
            return null;
        }));
        return result;
    }

    public void deleteQRCode(String paymentId) {
        String codeId = redisClient.get(OrderKey.PAYPAY_CODE_ID, paymentId);
        LOGGER.info("paymentId=>{}のQRCodeID=>{}を削除し始める", paymentId, codeId);
        if (StringUtils.isBlank(codeId)) {
            LOGGER.warn("paymentId=>{}にはQRCodeIDがないので、削除できない", paymentId);
            return;
        }
        try {
            NotDataResponse response = new PaymentApi(getApiClient()).deleteQRCode(codeId);
            LOGGER.info("paymentId=>{}のQRCodeID=>{}削除response=>{}", paymentId, codeId, JSON.toJSONString(response));
            if (SUCCESS.equals(response.getResultInfo().getCode())) {
                redisClient.removeKey(OrderKey.PAYPAY_CODE_ID, paymentId);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("paymentId=>%1$sのQRCodeID=>%2$s削除がエラーになった", paymentId, codeId), e);
        }
    }

    public PublicResult<String> getPayDetail(String merchantPaymentId) {
        if (StringUtils.isBlank(redisClient.get(OrderKey.PAYPAY_CODE_ID, merchantPaymentId))) {
            return PublicResult.error();
        }
        try {
            // Calling the method to get payment details
            PaymentDetails response = new PaymentApi(getApiClient()).getCodesPaymentDetails(merchantPaymentId);
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
        } catch (Exception e) {
            LOGGER.error(String.format("Paypay%1$s=>支払う状態の取得がエラーになった", merchantPaymentId), e);
            return PublicResult.error();
        }
    }

    private ApiClient getApiClient() throws ApiException {
        ApiClient apiClient = new Configuration().getDefaultApiClient();
        apiClient.setProductionMode(productionMode);
        apiClient.setApiKey(apiKey);
        apiClient.setApiSecretKey(secret);
        apiClient.setAssumeMerchant(assumeMerchant);
        return apiClient;
    }

}
