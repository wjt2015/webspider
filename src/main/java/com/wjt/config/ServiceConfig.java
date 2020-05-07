package com.wjt.config;

import com.wjt.service.impl.JueJinServiceImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {DBConfig.class, RedisConfig.class})
@ComponentScan(basePackageClasses = {JueJinServiceImpl.class})
public class ServiceConfig {
}
