package com.wjt.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Time 2020/4/22/3:01
 * @Author jintao.wang
 * @Description
 */
@Configuration
@ComponentScan(basePackages = {"com.wjt.service"})
public class BizConfig {
}
