package com.tmall.order.rabbitmq;

import com.tmall.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQReceiver {

//    public static final Logger LOGGER = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = MQConfig.ORDER_QUEUE)
    public void receiveOrder(String orderStr) {
        orderService.generateOrder(orderStr);
    }
}
