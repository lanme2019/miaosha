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
    @Value("${spring.redis.port}")
    private int redisPort;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private int redisMaxIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int redisMaxTotal;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private int redisMaxWaitMillis;
    /*****redis config end*******/

}
