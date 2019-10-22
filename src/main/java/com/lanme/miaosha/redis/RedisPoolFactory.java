package com.lanme.miaosha.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getRedisMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getRedisMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getRedisMaxWaitMillis() * 1000);
        JedisPool jp = new JedisPool(poolConfig, redisConfig.getRedisHost(), redisConfig.getRedisPort(),
                redisConfig.getTimeout()*1000, redisConfig.getPassword(), 0);
        return jp;
    }
}
