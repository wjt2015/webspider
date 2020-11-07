package com.wjt.config;

import com.wjt.service.JsoupService;
import com.wjt.service.JueJinService;
import com.wjt.service.impl.JsoupServiceImpl;
import com.wjt.service.impl.JueJinServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;
import redis.clients.jedis.MyJedisPool;

import java.util.HashMap;
import java.util.List;

@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {ServiceConfig.class, SpiderConfig.class})
public class ServiceConfigTest {

    //@Resource
    private JueJinService jueJinService;

    private MyAnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {

        this.applicationContext = new MyAnnotationConfigApplicationContext(ServiceConfig.class, SpiderConfig.class);

        OkHttpClient okHttpClient = applicationContext.getBean(OkHttpClient.class);
        JsoupService jsoupService = applicationContext.getBean(JsoupService.class);

        //this.jueJinService = applicationContext.getBean(JueJinService.class);

        //log.info("applicationContext={};jueJinService={};okHttpClient={};jsoupService={};", applicationContext, jueJinService, okHttpClient, jsoupService);

    }

    @Test
    public void save() {
        String url = "http://bbs.xjtu.edu.cn/BMY_B/home?B=PieBridge";
        log.info("start ...");
        jueJinService.getJuejinArticles(url);

        log.info("getJuejinArticles_finish!");
    }

    @Test
    public void getBeanNamesForType() {

        ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();

        Class<?> clazz = JsoupService.class;
        String[] beanNamesForType = beanFactory.getBeanNamesForType(clazz, true, true);
        log.info("clazz={};beanNamesForType={};", clazz, beanNamesForType);

        clazz = JsoupServiceImpl.class;
        beanNamesForType = beanFactory.getBeanNamesForType(clazz, true, true);
        log.info("clazz={};beanNamesForType={};", clazz, beanNamesForType);

        clazz = MyJedisPool.class;
        beanNamesForType = beanFactory.getBeanNamesForType(clazz, true, true);
        log.info("clazz={};beanNamesForType={};", clazz, beanNamesForType);
    }

    @Test
    public void getMergedBeanDefinition() {
        String beanName = "jsoupServiceImpl";
        ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();

        BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
        log.info("beanName={};beanDefinition={};", beanName, beanDefinition);

        beanName = "myJedisPool";
        beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
        log.info("beanName={};beanDefinition={};", beanName, beanDefinition);

    }


    private HashMap<Integer, List<String>> map = new HashMap<>();

    @Test
    public void resolvableType() throws NoSuchFieldException {

        Class<?> clazz = JueJinService.class;
        ResolvableType resolvableType = ResolvableType.forRawClass(clazz);
        log.info("clazz={};resolvableType={};", clazz, resolvableType);
        clazz = JueJinServiceImpl.class;
        ResolvableType rawResolvableType = ResolvableType.forRawClass(JueJinServiceImpl.class);
        resolvableType = ResolvableType.forClass(JueJinServiceImpl.class);
        log.info("clazz={};rawResolvableType={};resolvableType={};", clazz, rawResolvableType, resolvableType);

        log.info("primitive={};", Void.TYPE.isPrimitive());

        resolvableType = ResolvableType.forField(getClass().getDeclaredField("map"));

        ResolvableType superType = resolvableType.getSuperType();
        ResolvableType asMapType = resolvableType.asMap();
        ResolvableType[] typeInterfaces = resolvableType.getInterfaces();

        ResolvableType[] typeGenerics = resolvableType.getGenerics();

        log.info("resolvableType={};superType={};asMapType={};typeInterfaces={};typeGenerics={};",
                resolvableType, superType, asMapType, typeInterfaces, typeGenerics);


        resolvableType = ResolvableType.forInstance(this.jueJinService);
        typeGenerics = resolvableType.getGenerics();
        typeInterfaces = resolvableType.getInterfaces();
        asMapType = resolvableType.asMap();
        superType = resolvableType.getSuperType();

        log.info("resolvableType={};superType={};asMapType={};typeInterfaces={};typeGenerics={};",
                resolvableType, superType, asMapType, typeInterfaces, typeGenerics);
        //spring源码学习笔记

    }





}