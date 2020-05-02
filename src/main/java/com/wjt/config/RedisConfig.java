package com.wjt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @Time 2020/4/22/1:31
 * @Author jintao.wang
 * @Description
 */
@Slf4j
@Configuration
public class RedisConfig {
    @Bean
    public JedisPool jedisPool() {
        JedisPool jedisPool = new JedisPool("localhost", 6379);
/*        String ret = jedisPool.getResource().auth("linux2014");
        log.info("auth_ret={};", ret);*/
        return jedisPool;
    }


}
