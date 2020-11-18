package com.wjt.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MyMapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;


@Slf4j
@Configuration
//@PropertySource(value = {"classpath:/dao/jdbc.properties"})
@Import(value = {DataSourceConfig.class})
@EnableTransactionManagement
public class DBConfigBasedAnnotation {

    @javax.annotation.Resource
    private DataSourceConfig dataSourceConfig;

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
    private int queryTimeout;*/

    //----


    public DBConfigBasedAnnotation() {
        //log.info("driverClassName={};jdbcUrl={};", driverClassName, jdbcUrl);
        log.info("dataSourceConfig={};", dataSourceConfig);
    }

    @DependsOn(value = {"dataSourceConfig"})
    @Bean
    public DataSource dataSource() {
        log.info("driverClassName={};jdbcUrl={};", dataSourceConfig.driverClassName, dataSourceConfig.jdbcUrl);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dataSourceConfig.driverClassName);
        dataSource.setUrl(dataSourceConfig.jdbcUrl);
        dataSource.setUsername(dataSourceConfig.username);
        dataSource.setPassword(dataSourceConfig.password);
        dataSource.setMaxActive(dataSourceConfig.maxActive);
        dataSource.setMinIdle(dataSourceConfig.minIdle);
        dataSource.setDefaultAutoCommit(dataSourceConfig.defaultAutoCommit);
        dataSource.setQueryTimeout(dataSourceConfig.queryTimeout);
        log.info("dataSource={};", dataSource);
        return dataSource;
    }



/*    @Bean
    public DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig();
    }

    @Bean
    public DataSource dataSource(@Autowired DataSourceConfig dataSourceConfig) {
        log.info("dataSourceConfig={};", dataSourceConfig);
        //log.info("driverClassName={};jdbcUrl={};", driverClassName, jdbcUrl);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dataSourceConfig.driverClassName);
        dataSource.setUrl(dataSourceConfig.jdbcUrl);
        dataSource.setUsername(dataSourceConfig.username);
        dataSource.setPassword(dataSourceConfig.password);
        dataSource.setMaxActive(dataSourceConfig.maxActive);
        dataSource.setMinIdle(dataSourceConfig.minIdle);
        dataSource.setDefaultAutoCommit(dataSourceConfig.defaultAutoCommit);
        dataSource.setQueryTimeout(dataSourceConfig.queryTimeout);
        log.info("dataSource={};", dataSource);
        return dataSource;
    }*/


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
        //mapperScannerConfigurer.setProcessPropertyPlaceHolders(true);
        log.info("mapperScannerConfigurer={};", mapperScannerConfigurer);
        return mapperScannerConfigurer;
    }

    @Bean
    public static DataSourceTransactionManager transactionManager(@Autowired DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        log.info("dataSourceTransactionManager={};", dataSourceTransactionManager);
        return dataSourceTransactionManager;
    }

    /**
     * *
     * * 要想使用@Value 用${}占位符注入属性，这个bean是必须的，这个就是占位bean,另一种方式是不用value直接用Envirment变量直接getProperty('key');
     * * 警告: Cannot enhance @Configuration bean definition 'DBConfig' since its singleton instance has been created too early.
     * * The typical cause is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor return type: Consider declaring such methods as 'static'.
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
