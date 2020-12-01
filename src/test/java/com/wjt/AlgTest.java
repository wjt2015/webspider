package com.wjt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class AlgTest {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10, 20, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(20000));

    private static void grepConcurrently(final Pattern pattern, final String dir, final ConcurrentSkipListSet<String> foundSet, final ConcurrentSkipListSet<CompletableFuture<Void>> tasks) {
        File dirFile = new File(dir);
        for (File f : dirFile.listFiles()) {



            if (f.isFile() && f.canRead() && (f.getName().endsWith(".h") || f.getName().endsWith(".cpp"))) {
                /**
                 * 并发地检查每个源码文件;
                 */
/*
                tasks.add(CompletableFuture.runAsync(() -> {
                    grepFile(pattern, f, foundSet);
                }, THREAD_POOL_EXECUTOR));
*/

                grepFile(pattern, f, foundSet);

            } else if (f.isDirectory()) {
                /**
                 * 并发地检查每个目录下的源码文件;
                 */
/*                tasks.add(CompletableFuture.runAsync(() -> {
                    grepConcurrently(pattern, f.getAbsolutePath(), foundSet, tasks);
                }, THREAD_POOL_EXECUTOR));*/

                grepConcurrently(pattern, f.getAbsolutePath(), foundSet, tasks);

            }
        }

    }


    private static void grepFile(final Pattern pattern, final File f, final ConcurrentSkipListSet<String> foundSet) {

        if(f.getAbsolutePath().contains("libdeps")){
            return;
        }

        List<String> stringList = Collections.EMPTY_LIST;
        try {
            stringList = Files.readAllLines(Paths.get(f.getAbsolutePath()));
        } catch (IOException e) {
            log.error("read file error!f={};", f, e);
        }
        if (CollectionUtils.isNotEmpty(stringList)) {
            for (String line : stringList) {
                if (pattern.matcher(line).find()) {
                    foundSet.add(f.getAbsolutePath());
                    return;
                }
            }

        }
    }


    /**
     * 统计一个文件夹下某行字符串的出现情况;
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException     发现用并发任务,每次返回的tasks个数都不一致,为什么呢?因为每次都是动态地添加异步任务,只要把当前添加到的任务执行完毕就会退出,但是可能还没有添加完毕;
     *                              用同步的方法即可得到确切的结果;
     */
    @Test
    public void grepConcurrently() throws InterruptedException, ExecutionException, TimeoutException {
        long start = System.currentTimeMillis();

        final String dir = "/Users/jintao9/linux2014/";

        final ConcurrentSkipListSet<CompletableFuture<Void>> tasks = new ConcurrentSkipListSet<>(new Comparator<CompletableFuture<Void>>() {
            @Override
            public int compare(CompletableFuture<Void> o1, CompletableFuture<Void> o2) {
                return -1;
            }
        });
        Pattern pattern = Pattern.compile("notifyStats");
        ConcurrentSkipListSet<String> foundSet = new ConcurrentSkipListSet<>();
        grepConcurrently(pattern, dir, foundSet, tasks);

        //CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()])).get(1000,TimeUnit.MILLISECONDS);

        log.info("tasks.size={};elapsed={}ms;foundSet={};", tasks.size(), (System.currentTimeMillis() - start), foundSet);

        System.out.println("tasks.size=" + tasks.size() + ";elapsed=" + (System.currentTimeMillis() - start) + "ms;foundSet=" + foundSet);


        /**
         * 先查出所有的文本文件,再构建并发任务查询字符串,可明显降低查询时间;
         */
        start = System.currentTimeMillis();
        foundSet.clear();
        final HashSet<String> fileNames = new HashSet<>();
        findAllFiles(dir, fileNames);

        Set<CompletableFuture<Void>> futures = fileNames.stream().map(fileName -> {
            return CompletableFuture.runAsync(() -> {
                grepFile(pattern, new File(fileName), foundSet);
            }, THREAD_POOL_EXECUTOR);
        }).collect(Collectors.toSet());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

        System.out.println("futures.size=" + futures.size() + ";elapsed=" + (System.currentTimeMillis() - start) + "ms;foundSet=" + foundSet);


    }


    private static void findAllFiles(final String dir, final HashSet<String> fileNames) {
        final File dirFile = new File(dir);
        for (File f : dirFile.listFiles()) {
            if(f.getAbsolutePath().contains("libdeps")){
                continue;
            }
            if (f.isDirectory()) {
                findAllFiles(f.getAbsolutePath(), fileNames);
            } else if (f.canRead() && (f.getName().endsWith(".h") || f.getName().endsWith(".cpp"))) {
                fileNames.add(f.getAbsolutePath());
            }
        }

    }

}