package com.wjt.task;

import redis.clients.jedis.JedisPool;

public interface JedisTask {
    Object doTask(JedisPool jedisPool);
}
