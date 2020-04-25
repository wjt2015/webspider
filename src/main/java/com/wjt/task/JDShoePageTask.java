package com.wjt.task;

import com.wjt.common.Constants;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Time 2020/4/22/3:04
 * @Author jintao.wang
 * @Description 解析京东鞋子的展示页面;
 */
@Slf4j
@ToString
public class JDShoePageTask implements WebDriverTask {

    private final String pageUrl;

    private JedisPool jedisPool;

    public JDShoePageTask(String pageUrl, JedisPool jedisPool) {
        this.pageUrl = pageUrl;
        this.jedisPool = jedisPool;
    }

    @Override
    public boolean work(WebDriver webDriver) {
        try {
            webDriver.get(pageUrl);
            String nextPageUrl = nextPageUrl(webDriver);
            log.info("nextPageUrl={}", nextPageUrl);
            new PageUrlSaveTask(Constants.JD_PAGE_URL_LIST_KEY, nextPageUrl).doRedis(jedisPool);
            new PageUrlSaveTask(Constants.JD_PAGE_URL_LIST_KEY_COPY, nextPageUrl).doRedis(jedisPool);
        } catch (Exception e) {
            log.error("parse page error!webDriver={};pageUrl={};", webDriver, pageUrl, e);
            return false;
        }
        return true;
    }

    /**
     * 查找下一页按钮url;
     *
     * @param webDriver
     * @return
     */
    private String nextPageUrl(final WebDriver webDriver) {
        final String currentUrl = webDriver.getCurrentUrl();

        try {
            //找到下一页按钮,点击一下,然后获取当前页面url;
            WebElement nextElement = webDriver.findElement(By.xpath("//a[@class=\"pn-next\"]"));
            nextElement.click();
            return webDriver.getCurrentUrl();
        } catch (Exception e) {
            log.error("parse next url error!url={};", currentUrl, e);
        }
        return null;
    }

}
