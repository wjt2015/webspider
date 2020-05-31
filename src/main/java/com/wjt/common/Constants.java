package com.wjt.common;

import org.elasticsearch.threadpool.Scheduler;

import java.util.Random;
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

    //win10,
    //String CHROME_DRIVER_BIN = "D:\\soft\\chromedriver\\chromedriver_win32_81\\chromedriver.exe";

    //macos,

    String CHROME_DRIVER_BIN = "/Users/jintao9/linux2014/install_dir/chromedriver834103/chromedriver";

    //String PHANTOMJS_DRIVER_BIN = "D:\\soft\\chromedriver\\phantomjs-2.5.0-beta2-windows\\bin\\phantomjs.exe";

    ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 10, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(500));

    ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.DiscardOldestPolicy());

    String JD_PAGE_URL_LIST_KEY = "jd_page_url_list";
    String JD_PAGE_URL_LIST_KEY_COPY = "jd_page_url_list_copy";

    Random RANDOM = new Random();


}
