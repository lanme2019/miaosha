package com.lanme.miaosha.rabbitmq;

import com.lanme.miaosha.model.MiaoshaMessage;
import com.lanme.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/11/5 16:50
 */
@Service
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    Logger log = LoggerFactory.getLogger(MQSender.class);

    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String msg = RedisService.beanToString(message);
        log.info("send message:"+ msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }


}
