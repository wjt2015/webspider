package com.wjt.task;

import redis.clients.jedis.JedisPool;

/**
 * @Time 2020/4/22/4:30
 * @Author jintao.wang
 * @Description
 */
public interface RedisTask {
    Object doRedis(final JedisPool jedisPool);
}
