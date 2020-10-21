package com.wjt.service.impl;

import com.wjt.config.ServiceConfig;
import com.wjt.service.JsoupService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class JsoupServiceImplTest {

    @Resource
    private JsoupService jsoupService;

    @Test
    public void saveUniversityDetail() {
        log.info("jsoupService={};",jsoupService);
        String url="http://computer.upc.edu.cn/2017/0321/c6289a107120/page.htm";
        jsoupService.saveUniversityDetail(url);
    }

    @Test
    public void saveBingmayongBBS(){
        String url="http://bbs.xjtu.edu.cn/BMY_B/";
        url="https://bbs.fudan.edu.cn/v18/tdoc?board=Magpie_Bridge";
        jsoupService.saveBingmayongBBS(url);
    }
}