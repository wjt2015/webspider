package com.wjt.common;

import org.elasticsearch.threadpool.Scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Time 2020/4/14/22:33
 * @Author jintao.wang
 * @Description
 */
public interface Constants {

    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));

    ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.DiscardOldestPolicy());
}
