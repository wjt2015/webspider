package com.wjt.config;

import com.wjt.model.WebDriverWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Time 2020/4/22/1:30
 * @Author jintao.wang
 * @Description
 */
@Slf4j
@Configuration
@Import(value = {BizConfig.class, RedisConfig.class})
public class AppConfig {

    /**
     * 构造出可用的webDriver;
     *
     * @return
     */
    /*@Bean
    public WebDriverTeam webDriverTeam() throws IOException {
        final int n = 5;
        int i;

        List<ChromeDriverService> chromeDriverServices = new ArrayList<>(n);

        BlockingQueue<WebDriver> webDrivers = new ArrayBlockingQueue<>(n);
        for (i = 0; i < n; i++) {
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                    .usingAnyFreePort()
                    .usingDriverExecutable(new File(Constants.CHROME_DRIVER_BIN))
                    .build();
            chromeDriverService.start();
            chromeDriverServices.add(chromeDriverService);

            WebDriver webDriver = new ChromeDriver(chromeDriverService*//*, new ChromeOptions().addArguments("--headless")*//*);
            webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS)
                    .setScriptTimeout(30, TimeUnit.SECONDS)
                    .pageLoadTimeout(40, TimeUnit.SECONDS);
            webDrivers.offer(webDriver);
            webDriver.quit();
        }
        WebDriverTeam webDriverTeam = new WebDriverTeam(webDrivers, chromeDriverServices);
        //在程序退出时关闭webdriver;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            webDriverTeam.quit();
        }));

        log.info("webDriverTeam={};", webDriverTeam);

        return webDriverTeam;
    }
*/
    @Bean
    public WebDriverWorker webDriverWorker() {
        return new WebDriverWorker();
    }

}
