package com.wjt.config;

import com.wjt.task.MyJedisTask;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.MyJedis;
import redis.clients.jedis.MyJedisPool;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisConfig.class})
public class RedisConfigTest {

    @Resource
    private MyJedisPool myJedisPool;

    private int n = 1000;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1, 10, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    @After
    public void free(){
        myJedisPool.close();
        THREAD_POOL_EXECUTOR.shutdownNow();
    }

    @Test
    public void myJedisPool() {
        final long start = System.currentTimeMillis();
        int i;
        for (i = 0; i < n; i++) {
            final int t = i;
            new MyJedisTask() {
                @Override
                protected Object doJedisTask(MyJedis myJedis, Object o) {
                    o = String.valueOf(t);
                    myJedis.lpush("test_list", String.valueOf(t));
                    return null;
                }
            }.doTask(myJedisPool, null);
        }
        log.info("myJedisPool_finish!n={};elapsed={}ms;", n, (System.currentTimeMillis() - start));
    }

    @Test
    public void multiThreadJedisPool() {
        int i;
        n = 10000;
        final long start = System.currentTimeMillis();
        CompletableFuture[] tasks = new CompletableFuture[n];
        for (i = 0; i < n; i++) {
            final int t = i;
            tasks[i] = CompletableFuture.runAsync(() -> {
                new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        myJedis.lpush("multiThreadJedisPool", String.valueOf(t));
                        return null;
                    }
                }.doTask(myJedisPool, null);
            }, THREAD_POOL_EXECUTOR);
        }

        CompletableFuture.allOf(tasks).join();

        log.info("multiThreadJedisPool!n={};elapsed={}ms;", n, (System.currentTimeMillis() - start));
    }

    @Test
    public void multiThreadJedisPoolOneByOne() throws InterruptedException {
        final long start = System.currentTimeMillis();
        int i = 0;
        final List<Callable<Object>> tasks = new ArrayList<>(n);
        for (i = 0; i < n; i++) {
            final int t = i;
            tasks.add(() -> {
                new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        myJedis.pipelined().lpush("multiThreadJedisPoolOneByOne", String.valueOf(t));
                        return null;
                    }
                }.doTask(myJedisPool, null);

                return null;
            });
        }

        THREAD_POOL_EXECUTOR.invokeAll(tasks);
        log.info("multiThreadJedisPoolOneByOne_finish!n={};elapsed={}ms;", n, (System.currentTimeMillis() - start));
    }

    @Test
    public void pipeline() {
        final long start = System.currentTimeMillis();
        //每组个数;
        final int count = 20;
        final List<CompletableFuture<Void>> tasks = new ArrayList<>();

        for (int i = 0, t = i; i < n; i = t + 1) {
            final List<String> idList = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                idList.add(String.valueOf(t = i + j));
            }
            tasks.add(CompletableFuture.runAsync(() -> {
                new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        myJedis.pipelined().lpush("pipeline", idList.toArray(new String[idList.size()]));
                        return null;
                    }
                }.doTask(myJedisPool, null);
            }, THREAD_POOL_EXECUTOR));
        }

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()])).join();
        log.info("pipeline_finish!n={};elapsed={}ms;", n, (System.currentTimeMillis() - start));
    }

    @Test
    public void myJedis() {
        myJedisPool();
        multiThreadJedisPool();
    }

}