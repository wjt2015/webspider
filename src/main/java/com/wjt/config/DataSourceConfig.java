package com.wjt.config;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@ToString
@Configuration(value = "dataSourceConfig")
@PropertySource(value = {"classpath:/dao/jdbc.properties"})
public class DataSourceConfig {

    @Value("${driverClassName}")
    public String driverClassName;

    @Value("${wjt_train_jdbc_url}")
    public String jdbcUrl;

    @Value("${username}")
    public String username;

    @Value("${password}")
    public String password;

    @Value("${max_active}")
    public int maxActive;

    @Value("${min_idle}")
    public int minIdle;

    @Value("${default_auto_commit}")
    public boolean defaultAutoCommit;

    @Value("${query_timeout}")
    public int queryTimeout;

    public DataSourceConfig() {
        log.info("driverClassName={};jdbcUrl={};", driverClassName, jdbcUrl);
    }
}
