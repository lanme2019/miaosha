package com.lanme.miaosha.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/11/5 15:13
 */
@Configuration
public class MQConfig {

    public static final String QUEUE = "queue";

        @Bean
        public Queue queue() {
            return new Queue(QUEUE,true);
        }
}
