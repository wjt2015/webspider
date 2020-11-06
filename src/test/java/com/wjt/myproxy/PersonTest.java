package com.wjt.myproxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * (https://www.bilibili.com/video/BV1Qb411u7Yy?p=2);
 * spring源码,代理模式;
 */
@Slf4j
public class PersonTest {
    @Test
    public void findLove() throws IOException {

        XiaoXingXing xiaoXingXing = new XiaoXingXing();
        MeiPo meiPo = new MeiPo(xiaoXingXing);

        Person proxy = (Person) meiPo.getInstance();

        proxy.findLove();

        log.info("proxy={};proxy.Class={};proxy is null?{};", proxy, proxy.getClass(), (proxy == null));


        byte[] bytes = ProxyGenerator.generateProxyClass("com.sun.proxy.$Proxy4", new Class<?>[]{Person.class});

        log.info("bytes={};",bytes);

        log.info("str={};",new String(bytes));

        String fileName="/Users/jintao9/linux2014/wjt_projs/webspider/docs/logs/proxy.txt";
        Files.write(Paths.get(fileName),bytes);

    }

}