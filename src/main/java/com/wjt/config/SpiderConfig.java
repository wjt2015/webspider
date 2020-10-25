package com.wjt.config;

import com.wjt.common.Constants;
import com.wjt.service.impl.WebdriverServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@Import(value = {RedisConfig.class, DBConfig.class})
@ComponentScan(basePackageClasses = {WebdriverServiceImpl.class})
public class SpiderConfig {

    static {
        System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_BIN);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();

        return okHttpClient;
    }

    @Bean
    public WebDriver chromeDriver() {
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);

        return chromeDriver;
    }

    @Bean
    public WebDriver chromeDriverForDetail() {
        final ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);

        return chromeDriver;
    }


}
