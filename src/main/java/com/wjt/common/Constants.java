package com.wjt.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Time 2020/4/14/22:33
 * @Author jintao.wang
 * @Description
 */
public interface Constants {

    ExecutorService EXECUTOR_SERVICE=new ThreadPoolExecutor(5,10,100,TimeUnit.SECONDS,new ArrayBlockingQueue<>(500));

}
