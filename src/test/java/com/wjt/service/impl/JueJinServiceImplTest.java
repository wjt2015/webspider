package com.wjt.service.impl;

import com.wjt.common.Constants;
import com.wjt.config.ServiceConfig;
import com.wjt.service.JueJinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class JueJinServiceImplTest {

    static {
        System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_BIN);
    }


    @Resource
    private JueJinService jueJinService;
    @Resource
    private JedisPool jedisPool;

    @Before
    public void init() {

        //jedisPool.getResource().del("next_url_list");
    }

    @Test
    public void getJuejinArticles() {

        //final String url = "https://juejin.im/post/5eae84daf265da7bf7328e25";
        final String url = "https://juejin.im/post/5eae84daf265da7bf7328e25";
        final long start = System.currentTimeMillis();
        jueJinService.getJuejinArticles(url);

        final long elapsed = System.currentTimeMillis() - start;
        log.info("complete!elapsed={}ms;", elapsed);
    }

    @Test
    public void saveRelationship(){
        final String startUrl="https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&clk1=3bab53c4056adfaf56cccea3ff80bea4&keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B&page=0";
        jueJinService.saveRelationship(startUrl);

    }
}