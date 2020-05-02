package com.wjt.model;

import com.wjt.task.WebDriverTask;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Time 2020/4/22/2:30
 * @Author jintao.wang
 * @Description
 */
@Slf4j
@ToString
public class WebDriverTeam {

    private final List<ChromeDriverService> chromeDriverServices;
    private final List<WebDriver> allWebDrivers;
    private final BlockingQueue<WebDriver> webDrivers;

    public WebDriverTeam(BlockingQueue<WebDriver> webDrivers, List<ChromeDriverService> chromeDriverServices) {
        this.webDrivers = webDrivers;
        allWebDrivers = new ArrayList<>(webDrivers);
        this.chromeDriverServices = chromeDriverServices;
    }

    /**
     * 根据任务的执行情况决定是否重做;
     *
     * @param task
     * @return
     */
    public boolean doTask(WebDriverTask task) {
        try {
            WebDriver webDriver = webDrivers.poll(30, TimeUnit.SECONDS);
            if (webDriver == null) {
                return false;
            }
            final boolean ret = task.work(webDriver);
            //boolean offer = webDrivers.offer(webDriver);
            //log.info("ret={};offer={};webDriver={};", ret, offer, webDriver);
            return ret;
        } catch (Exception e) {
            log.error("work error!task={};", task, e);
            return false;
        }
    }

    public void quit() {
        log.info("start to quit!webDrivers={};allWebDrivers={};", webDrivers, allWebDrivers);
        for (WebDriver webDriver : allWebDrivers) {
            webDriver.quit();
        }
        for (ChromeDriverService chromeDriverService : chromeDriverServices) {
            chromeDriverService.stop();
        }
        log.info("quit finish!webDrivers={};allWebDrivers={};", webDrivers, allWebDrivers);
    }

}
