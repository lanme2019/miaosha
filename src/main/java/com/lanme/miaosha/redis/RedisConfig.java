package com.lanme.miaosha.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lanme2019
 * @version 1.0
 * @date 2019/10/22 11:21
 */


@Component
@Data
public class RedisConfig {
    /*****redis config start*******/
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${redis.max-idle}")
    private int redisMaxTotal;
    @Value("${redis.max-total}")
    private int redisMaxIdle;
    @Value("${redis.max-wait-millis}")
    private int redisMaxWaitMillis;
    /*****redis config end*******/

}
