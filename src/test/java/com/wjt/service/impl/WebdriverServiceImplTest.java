package com.wjt.service.impl;

import com.wjt.config.SpiderConfig;
import com.wjt.service.WebdriverService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 *
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpiderConfig.class})
public class WebdriverServiceImplTest {

    @Resource
    private WebdriverService webdriverService;

    /**
     * mvn clean test -Dtest=com.wjt.service.impl.WebdriverServiceImplTest#saveRiyueguanghuaBBS
     */
    @Test
    public void saveRiyueguanghuaBBS() {
        String url = "https://bbs.fudan.edu.cn/v18/tdoc?board=Magpie_Bridge";
        webdriverService.saveRiyueguanghuaBBS(url);

    }
}