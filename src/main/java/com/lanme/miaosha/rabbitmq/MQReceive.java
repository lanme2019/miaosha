package com.lanme.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/11/5 16:50
 */
@Service
public class MQReceive {

    private static Logger logger = LoggerFactory.getLogger(MQReceive.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        logger.info("receive message"+message);
    }

}
