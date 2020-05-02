package com.wjt.model;

import com.wjt.task.WebDriverTask;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * @Time 2020/4/22/18:49
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class WebDriverWorker {

    /**
     * 根据任务的执行情况决定是否重做;
     *
     * @param task
     * @return
     */
    public boolean doTask(WebDriverTask task) {

        try {
            WebDriver webDriver = new ChromeDriver();
            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)
                    .setScriptTimeout(30, TimeUnit.SECONDS)
                    .pageLoadTimeout(30, TimeUnit.SECONDS);
            final boolean ret = task.work(webDriver);
            webDriver.quit();
            return ret;
        } catch (Exception e) {
            log.error("doTask error!task={};", task, e);
            return false;
        }
    }

}
