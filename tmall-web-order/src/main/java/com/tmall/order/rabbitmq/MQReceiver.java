package com.tmall.order.rabbitmq;

import com.tmall.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MQReceiver {

//    public static final Logger LOGGER = LoggerFactory.getLogger(MQReceiver.class);

    @Resource
    private OrderService orderService;

    @RabbitListener(queues = MQConfig.ORDER_QUEUE)
    public void receiveOrder(String orderStr) {
        orderService.generateOrder(orderStr);
    }
}
