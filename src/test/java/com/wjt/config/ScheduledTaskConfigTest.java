package com.wjt.config;

import com.wjt.service.ScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;

import static org.junit.Assert.*;

@Slf4j
public class ScheduledTaskConfigTest {

    private MyAnnotationConfigApplicationContext applicationContext;


    @Before
    public void init() {
        this.applicationContext = new MyAnnotationConfigApplicationContext(ScheduledTaskConfig.class);
        Object obj = null;
        ScheduledTask scheduledTask = null;
        obj = this.applicationContext.getBean("&scheduledTaskFactoryBeanA", ScheduledTask.class);
        log.info("obj={};", obj);

  /*
        obj = this.applicationContext.getBean("&scheduledTaskFactoryBeanA");
        log.info("obj={};", obj);

     scheduledTask= this.applicationContext.getBean(ScheduledTask.class);
        log.info("scheduledTask={};",scheduledTask);

        FactoryBean factoryBean = this.applicationContext.getBean(FactoryBean.class);
        log.info("factoryBean={};",factoryBean);
*/
    }

    @Test
    public void scheduledTaskFactoryBeanA() {
    }
}