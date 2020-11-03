package com.wjt.config;

import com.wjt.service.JueJinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, SpiderConfig.class})
public class ServiceConfigTest {

    @Resource
    private JueJinService jueJinService;

    @Before
    public void init() {

        MyAnnotationConfigApplicationContext applicationContext = new MyAnnotationConfigApplicationContext(ServiceConfig.class, SpiderConfig.class);
        this.jueJinService = applicationContext.getBean(JueJinService.class);
        log.info("applicationContext={};jueJinService={};", applicationContext, jueJinService);

    }

    @Test
    public void save() {
        String url = "http://bbs.xjtu.edu.cn/BMY_B/home?B=PieBridge";

        jueJinService.getJuejinArticles(url);

        log.info("getJuejinArticles_finish!");

    }

}