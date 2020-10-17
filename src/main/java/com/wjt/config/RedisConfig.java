package com.wjt.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.MyJedisPool;

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

        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxWaitMillis(2000);
        genericObjectPoolConfig.setMinIdle(5);
        genericObjectPoolConfig.setMaxTotal(50);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setTestOnCreate(true);
        genericObjectPoolConfig.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, "localhost", 6379);

/*        String ret = jedisPool.getResource().auth("linux2014");
        log.info("auth_ret={};", ret);*/
        return jedisPool;
    }

    @Bean
    public MyJedisPool myJedisPool() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxWaitMillis(100);
        genericObjectPoolConfig.setMinIdle(5);
        genericObjectPoolConfig.setMaxTotal(100);
        genericObjectPoolConfig.setTestOnBorrow(true);
        genericObjectPoolConfig.setTestOnCreate(true);
        //genericObjectPoolConfig.setMaxIdle(10);

        MyJedisPool myJedisPool = new MyJedisPool(genericObjectPoolConfig, "localhost", 6379);

        log.info("myJedisPool={};", myJedisPool);
        return myJedisPool;
    }


}
