package com.wjt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Time 2020/4/22/1:31
 * @Author jintao.wang
 * @Description
 */
@Configuration
@ImportResource(locations = {"classpath:dao/mybatis_spring.xml"})
public class DBConfig {


}
