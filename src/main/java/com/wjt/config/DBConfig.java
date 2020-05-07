package com.wjt.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @Time 2020/4/22/1:31
 * @Author jintao.wang
 * @Description
 */

@Slf4j
@Configuration
//@PropertySource(value = {"classpath:dao/jdbc.properties"})
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
//@ImportResource(locations = {"classpath:dao/mybatis_spring.xml"})
public class DBConfig {


    //@Value("${driverClassName}")
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    //@Value("${wjt_train_jdbc_url}")
    private String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/web_train?useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&connectTimeout=1000&socketTimeout =1000&autoReconnect=true";

    //@Value("${username}")
    private String username = "root";

    //@Value("${password}")
    private String password = "linux2014";

    //@Value("${max_active}")
    private int maxActive = 20;

    //@Value("${min_idle}")
    private int minIdle = 2;

    //@Value("${default_auto_commit}")
    private boolean defaultAutoCommit = true;

    //@Value("${query_timeout}")
    private int queryTimeout = 2000;

    @Bean
    public DataSource dataSource(@Autowired PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer) {

        log.info("driverClassName={};", driverClassName);

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);
        dataSource.setQueryTimeout(queryTimeout);
        log.info("dataSource={};", dataSource);
        return dataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);

        Resource resource = resourcePatternResolver.getResource("classpath:dao/mybatis_config.xml");
        sqlSessionFactoryBean.setConfigLocation(resource);

        log.info("resources={};resource={};sqlSessionFactoryBean={};", resources, resource, sqlSessionFactoryBean);
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(@Autowired SqlSessionFactoryBean sqlSessionFactoryBean) {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setAnnotationClass(Repository.class);
        mapperScannerConfigurer.setBasePackage("com.wjt.dao");
        log.info("mapperScannerConfigurer={};", mapperScannerConfigurer);
        return mapperScannerConfigurer;
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Autowired DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);

        log.info("dataSourceTransactionManager={};", dataSourceTransactionManager);
        return dataSourceTransactionManager;
    }


    //要想使用@Value 用${}占位符注入属性，这个bean是必须的，这个就是占位bean,另一种方式是不用value直接用Envirment变量直接getProperty('key')
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        log.info("propertySourcesPlaceholderConfigurer={};", propertySourcesPlaceholderConfigurer);
        return propertySourcesPlaceholderConfigurer;
    }

}
