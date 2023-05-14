package com.ameshima.order.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.ameshima.order.keys.OrderKey;
import com.ameshima.common.constants.MallConstant;
import com.ameshima.common.redis.RedisClient;
import com.ameshima.order.constants.OrderConstants;
import com.ameshima.order.entity.dto.OrderMQDTO;
import com.ameshima.order.utils.OrderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MQSender {

    public static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private OrderUtils orderUtils;
    @Resource
    private RedisClient redisClient;

    public String sendOrder(OrderMQDTO order) {
        try {
            String parentNo = orderUtils.generateOrderNo();
            order.setParentOrderNo(parentNo);
            order.getStoreGoodsList().forEach(store -> store.setOrderNo(orderUtils.generateOrderNo()));
            String msgStr = JSONObject.toJSONString(order);
            LOGGER.info("注文のメッセージを送り始める => {}", msgStr);
            amqpTemplate.convertAndSend(MQConfig.ORDER_QUEUE, msgStr);
            if (redisClient.set(OrderKey.ORDER_MQ, parentNo + MallConstant.UNDERLINE + order.getAccountId(), OrderConstants.OrderMqState.DEFAULT.getState())) {
                return parentNo;
            }
        } catch (Exception e) {
            LOGGER.error("注文のメッセージを送るのはエラーが発生した", e);
        }
        return null;
    }

}
