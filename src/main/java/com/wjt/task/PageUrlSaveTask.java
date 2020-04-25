package com.wjt.task;

import redis.clients.jedis.JedisPool;

/**
 * @Time 2020/4/22/4:31
 * @Author jintao.wang
 * @Description
 */
public class PageUrlSaveTask implements RedisTask {

    public String key;
    public String pageUrl;

    public PageUrlSaveTask(String key, String pageUrl) {
        this.key = key;
        this.pageUrl = pageUrl;
    }

    @Override
    public Object doRedis(JedisPool jedisPool) {
        return jedisPool.getResource().lpush(key, pageUrl);
    }
}
