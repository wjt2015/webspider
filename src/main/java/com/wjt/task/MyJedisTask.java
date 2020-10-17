package com.wjt.task;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.MyJedis;
import redis.clients.jedis.MyJedisPool;

@Slf4j
public abstract class MyJedisTask {
    public Object doTask(final MyJedisPool myJedisPool, final Object obj) {
        final long start = System.currentTimeMillis();
        Object ret = null;
        try (MyJedis myJedis = myJedisPool.getResource()) {
            ret = doJedisTask(myJedis, obj);
        } catch (Exception e) {
            log.error("myJedisTask_error!args={};", obj, e);
        } finally {
            log.info("myJedisTask_finish!obj={};elapsed={}ms;", obj, (System.currentTimeMillis() - start));
        }
        return ret;
    }

    protected abstract Object doJedisTask(final MyJedis myJedis, final Object o);
}
