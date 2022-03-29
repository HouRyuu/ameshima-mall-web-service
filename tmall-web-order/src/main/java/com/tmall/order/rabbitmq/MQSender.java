package com.tmall.order.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.tmall.common.constants.TmallConstant;
import com.tmall.common.redis.RedisClient;
import com.tmall.order.constants.OrderConstants;
import com.tmall.order.entity.dto.OrderMQDTO;
import com.tmall.order.keys.OrderKey;
import com.tmall.order.utils.OrderUtils;
import com.tmall.remote.goods.vo.ShopCartVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

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
            if (redisClient.set(OrderKey.ORDER_MQ, parentNo + order.getAccountId(), OrderConstants.OrderMqState.DEFAULT.getState())) {
                return parentNo;
            }
        } catch (Exception e) {
            LOGGER.error("注文のメッセージを送るのはエラーが発生した", e);
        }
        return null;
    }

}
