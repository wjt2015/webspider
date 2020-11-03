package com.wjt.config;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@ToString
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
    
}
