package com.wjt.service.impl;

import com.wjt.service.ExistChecker;
import com.wjt.task.JedisTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service
public class ExistCheckerImpl implements ExistChecker {

    @Resource
    private JedisPool jedisPool;

    private final String URL_SET = "url_set";

/*    @Override
    public List<String> exist(List<String> urls) {
        final long start = System.currentTimeMillis();
        List<String> existUrls = new ArrayList<>(urls.size());
        for (String url : urls) {
            if (jedisPool.getResource().sismember(URL_SET, url)) {
                existUrls.add(url);
            } else {
                jedisPool.getResource().sadd(URL_SET, url);
            }
            log.info("check existence;url={};", url);
        }
        log.info("check existence complete!urls.size={};elapsed={}ms;urls={};",
                urls.size(), (System.currentTimeMillis() - start), urls);
        return existUrls;
    }*/

    @Override
    public List<String> exist(final List<String> urls) {
        final long start = System.currentTimeMillis();
        log.info("start to check existence!urls.size={};urls={};", urls.size(), urls);
        if (CollectionUtils.isEmpty(urls)) {
            return Collections.EMPTY_LIST;
        }
        Map<String, Response<Boolean>> retMap = new HashMap<>();
        List<String> existUrls = new ArrayList<>(urls.size());

        JedisTask jedisTask = new JedisTask() {
            @Override
            public Object doTask(JedisPool jedisPool) {
                try (Pipeline pipeline = jedisPool.getResource().pipelined()) {
                    for (String url : urls) {
                        retMap.put(url, pipeline.sismember(URL_SET, url));
                    }
                    pipeline.sync();
                } catch (Exception e) {
                    log.error("check existence error!urls={}", urls, e);
                }
                return null;
            }
        };
        jedisTask.doTask(this.jedisPool);

        urls.forEach(url -> {
            if (retMap.get(url).get()) {
                existUrls.add(url);
            }
        });

        log.info("existUrls.size={};existUrls={};", existUrls.size(), existUrls);

        urls.removeAll(existUrls);

        if (CollectionUtils.isNotEmpty(urls)) {
            jedisTask = new JedisTask() {
                @Override
                public Object doTask(JedisPool jedisPool) {
                    try (Pipeline pipelineSave = jedisPool.getResource().pipelined()) {
                        urls.forEach(url -> pipelineSave.sadd(URL_SET, url));
                        pipelineSave.sync();
                    } catch (Exception e) {
                        log.error("add new url into redis!urls={}", urls, e);
                    }
                    return null;
                }
            };
            jedisTask.doTask(jedisPool);
        }
        log.info("check existence complete!urls.size={};elapsed={}ms;urls={};",
                urls.size(), (System.currentTimeMillis() - start), urls);
        return existUrls;
    }


}
