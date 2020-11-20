package com.wjt.config;

import com.wjt.aspect.DBAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackageClasses = {DBAspect.class})
public class AopConfig {
}
