package com.wjt.task;

import redis.clients.jedis.Jedis;

public interface JedisWork {
    Object doWork(Jedis jedis);
}
