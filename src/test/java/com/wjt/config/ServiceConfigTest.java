package com.wjt.config;

import com.wjt.service.JsoupService;
import com.wjt.service.JueJinService;
import com.wjt.service.impl.JsoupServiceImpl;
import com.wjt.service.impl.JueJinServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.MyDefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import redis.clients.jedis.MyJedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {ServiceConfig.class, SpiderConfig.class})
public class ServiceConfigTest {

    //@Resource
    private JueJinService jueJinService;

    private MyAnnotationConfigApplicationContext applicationContext;

    private MyDefaultListableBeanFactory beanFactory;

    @Before
    public void init() {

        OkHttpClient okHttpClient = null;
        JsoupService jsoupService = null;

        this.applicationContext = new MyAnnotationConfigApplicationContext(ServiceConfig.class, SpiderConfig.class);
        this.beanFactory = (MyDefaultListableBeanFactory) (this.applicationContext.getBeanFactory());

        //okHttpClient=(OkHttpClient) this.applicationContext.getBean("okHttpClient");


/*        okHttpClient = applicationContext.getBean(OkHttpClient.class);
        jsoupService = applicationContext.getBean(JsoupService.class);*/

        //this.jueJinService = applicationContext.getBean(JueJinService.class);

        log.info("applicationContext={};jueJinService={};okHttpClient={};jsoupService={};", applicationContext, jueJinService, okHttpClient, jsoupService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            WebDriver chromeDriver = this.applicationContext.getBean("chromeDriver", WebDriver.class);
            WebDriver chromeDriverForDetail = this.applicationContext.getBean("chromeDriverForDetail", WebDriver.class);
            chromeDriver.quit();
            chromeDriverForDetail.quit();
            log.info("webDriver quit!");
        }));
    }

    @After
    public void destroy() {
    }

    @Test
    public void getBeanDefinition() {

        String name = "okHttpClient";

/*        RootBeanDefinition rootBeanDefinition = null;
        BeanDefinition beanDefinition = this.beanFactory.getMergedBeanDefinition(name);
        log.info("beanDefinition={};", beanDefinition);

        name = "chromeDriverForDetail";
        beanDefinition = this.beanFactory.getMergedBeanDefinition(name);

        rootBeanDefinition = (RootBeanDefinition) beanDefinition;
        log.info("beanDefinition={};rootBeanDefinition={};targetType={};beanClassName={};", beanDefinition, rootBeanDefinition, rootBeanDefinition.getTargetType(), rootBeanDefinition.getBeanClassName());

        name = "okHttpClient";
        //this.beanFactory.createBean(name,rootBeanDefinition);*/

        Object bean = this.applicationContext.getBean(name);
        log.info("bean={};", bean);

    }

    @Test
    public void save() {
        String url = "http://bbs.xjtu.edu.cn/BMY_B/home?B=PieBridge";
        log.info("start ...");
        jueJinService.getJuejinArticles(url);

        log.info("getJuejinArticles_finish!");

        ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
        //beanFactory.addBeanPostProcessor();
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

    @Test
    public void isTypeMatch() {

        String beanName = "okHttpClient";
        boolean typeMatch = this.applicationContext.isTypeMatch(beanName, OkHttpClient.class);
        log.info("typeMatch={};", typeMatch);


        final OkHttpClient okHttpClientA = this.applicationContext.getBean(OkHttpClient.class);
        final OkHttpClient okHttpClientB = this.applicationContext.getBean(OkHttpClient.class);
        log.info("equals? {};okHttpClientA={};okHttpClientB={};", okHttpClientA == okHttpClientB, okHttpClientA, okHttpClientB);


    }

    @Test
    public void beanNamesForType() {

        ConfigurableListableBeanFactory beanFactory = this.applicationContext.getBeanFactory();
        String[] beanNamesForType = null;

        beanNamesForType = beanFactory.getBeanNamesForType(OkHttpClient.class, true, true);
        log.info("beanNamesForType={};", beanNamesForType);

/*        beanNamesForType = beanFactory.getBeanNamesForType(WebDriver.class, true, true);
        log.info("beanNamesForType.length={};beanNamesForType={};", beanNamesForType.length, Arrays.asList(beanNamesForType));
    */

    }

    /**
     * 资源文件解析;
     * 参考:
     * Resource resource = this.resourceLoader.getResource(resolvedLocation);
     * addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
     * PropertiesLoaderUtils
     * public static Properties loadProperties(EncodedResource resource)
     */
    @Test
    public void resource() throws IOException {
        String location = "classpath:dao/jdbc.properties";
        Resource resource = this.applicationContext.getResource(location);
        log.info("resource={};", resource);
        Properties properties = PropertiesLoaderUtils.loadProperties(resource);
        log.info("properties={};", properties);

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