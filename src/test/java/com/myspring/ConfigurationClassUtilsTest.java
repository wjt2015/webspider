package com.myspring;

import com.wjt.service.impl.JueJinServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@Slf4j
public class ConfigurationClassUtilsTest {

    private MyAnnotationConfigApplicationContext applicationContext;

    private MetadataReaderFactory metadataReaderFactory;

    @Before
    public void init(){
        applicationContext=new MyAnnotationConfigApplicationContext();
        applicationContext.setEnvironment(new StandardEnvironment());


        metadataReaderFactory=new CachingMetadataReaderFactory();

    }

    @Test
    public void processConfigBeanDefinitions() {
    }

    @Test
    public void configClazzToRootBeanDefinition() {
    }

    @Test
    public void findAllComponentScanClasses() throws IOException, ClassNotFoundException {
        final Set<String> processedClassNameSet=new HashSet<>();
        Set<Class<?>> allComponentScanClasses = ConfigurationClassUtils.findAllComponentScanClasses(JueJinServiceImpl.class, applicationContext.getEnvironment(), applicationContext,
                metadataReaderFactory, processedClassNameSet);

        log.info("processedClassNameSet={};allComponentScanClasses={};",processedClassNameSet,allComponentScanClasses);


    }

    @Test
    public void classLoader() throws IOException {
        String path="com/wjt/service/impl/";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> resources = classLoader.getResources(path);

        while(resources.hasMoreElements()){
            URL url = resources.nextElement();
            log.info("url={};",url);
        }



    }
}