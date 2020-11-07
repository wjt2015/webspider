package com.wjt.myproxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.ProxyGenerator;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
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


    @Test
    public void javaCompiler(){

        String srcFileNameA,srcFileNameB;
        srcFileNameA="/Users/jintao9/linux2014/wjt_projs/webspider/src/test/java/com/wjt/config/AppConfigTest.java";
        srcFileNameB="/Users/jintao9/linux2014/wjt_projs/webspider/src/main/java/com/wjt/service/impl/BloomFilterImpl.java";

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

        try (StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, null, null)){
            Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(srcFileNameA, srcFileNameB);

            Boolean call = javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjects).call();

            log.info("call={};javaFileObjects={};",call,javaFileObjects);
        }catch (Exception e){
            log.error("java compile error!",e);
        }



    }

}