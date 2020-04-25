package com.wjt.task;

import redis.clients.jedis.JedisPool;

/**
 * @Time 2020/4/22/4:46
 * @Author jintao.wang
 * @Description
 */
public class PageUrlGetTask implements RedisTask {

    private String key;

    public String pageUrl;

    public PageUrlGetTask(String key) {
        this.key = key;
    }

    @Override
    public Object doRedis(JedisPool jedisPool) {
        pageUrl = jedisPool.getResource().rpop(key);
        return pageUrl;
    }
}
