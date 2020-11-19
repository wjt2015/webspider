package com.wjt.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MyMapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
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
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
//@ImportResource(locations = {"classpath:dao/mybatis_spring.xml"})
//@ActiveProfiles(profiles = {"dev"})
@PropertySource(value = {"classpath:dao/jdbc.properties"})
public class DBConfigParam {


/*
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
*/

    //----
/*

    @Value("${driverClassName}")
    private String driverClassName;

    @Value("${wjt_train_jdbc_url}")
    private String jdbcUrl;

    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${max_active}")
    private int maxActive;

    @Value("${min_idle}")
    private int minIdle;

    @Value("${default_auto_commit}")
    private boolean defaultAutoCommit;

    @Value("${query_timeout}")
    private int queryTimeout;

    //----


    //@DependsOn(value = {"DBConfig"})
    @Bean
    public DataSource dataSource() {
        log.info("driverClassName={};jdbcUrl={};", driverClassName, jdbcUrl);
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



*/


    @Bean
    public DataSource dataSource(@Value("${driverClassName}") String driverClassName, @Value("${wjt_train_jdbc_url}") String jdbcUrl,
                                 @Value("${username}") String username, @Value("${password}") String password,
                                 @Value("${max_active}") int maxActive, @Value("${min_idle}") int minIdle,
                                 @Value("${default_auto_commit}") boolean defaultAutoCommit,
                                 @Value("${query_timeout}") int queryTimeout) {
        //public DataSource dataSource(@Autowired DataSourceConfig dataSourceConfig) {
        //log.info("dataSourceConfig={};", dataSourceConfig);
        //log.info("driverClassName={};jdbcUrl={};", driverClassName, jdbcUrl);
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


    @Bean(value = "sqlSessionFactoryBean")
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
    public MyMapperScannerConfigurer mapperScannerConfigurer() {
        MyMapperScannerConfigurer mapperScannerConfigurer = new MyMapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setAnnotationClass(Repository.class);
        mapperScannerConfigurer.setBasePackage("com.wjt.dao");
        mapperScannerConfigurer.setProcessPropertyPlaceHolders(true);
        log.info("mapperScannerConfigurer={};", mapperScannerConfigurer);
        return mapperScannerConfigurer;
    }

    @Bean
    public static DataSourceTransactionManager transactionManager(@Autowired DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        log.info("dataSourceTransactionManager={};", dataSourceTransactionManager);
        return dataSourceTransactionManager;
    }


    /*     * 要想使用@Value 用${}占位符注入属性，这个bean是必须的，这个就是占位bean,另一种方式是不用value直接用Envirment变量直接getProperty('key');
     * 警告: Cannot enhance @Configuration bean definition 'DBConfig' since its singleton instance has been created too early.
     * The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.
     *
     * @return
     */


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

        log.info("propertySourcesPlaceholderConfigurer={};", propertySourcesPlaceholderConfigurer);
        return propertySourcesPlaceholderConfigurer;
    }


}
