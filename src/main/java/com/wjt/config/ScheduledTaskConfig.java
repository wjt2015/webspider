package com.wjt.config;

import com.wjt.service.impl.ScheduledTaskFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FactoryBean,
 * (https://juejin.im/post/6890866042747387917);
 * (https://zhuanlan.zhihu.com/p/97005407);
 * (https://www.cnblogs.com/aspirant/p/9082858.html);
 */
@Slf4j
@Configuration
public class ScheduledTaskConfig {

    @Bean
    public ScheduledTaskFactoryBean scheduledTaskFactoryBeanA(){
        String cronStr="0 15 10 * * ?";
        return new ScheduledTaskFactoryBean(cronStr);
    }

    @Bean
    public ScheduledTaskFactoryBean scheduledTaskFactoryBeanB(){
        String cronStr="0 15 13 * * ?";
        return new ScheduledTaskFactoryBean(cronStr);
    }
}
