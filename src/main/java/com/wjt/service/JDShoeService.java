package com.wjt.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.wjt.common.CommonUtils;
import com.wjt.common.Constants;
import com.wjt.model.WebDriverWorker;
import com.wjt.task.JDShoePageTask;
import com.wjt.task.PageUrlGetTask;
import com.wjt.task.PageUrlSaveTask;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Time 2020/4/22/3:21
 * @Author jintao.wang
 * @Description 爬取jd鞋子子数据,练习nlp和推荐;
 */
@Slf4j
@Service
public class JDShoeService {

    @Resource
    private WebDriverWorker webDriverWorker;
    //private WebDriverTeam webDriverTeam;

    @Resource
    private JedisPool jedisPool;

    public void parsePageUrls(final String startPageUrl) {
        String curPageUrl = startPageUrl;

        new PageUrlSaveTask(Constants.JD_PAGE_URL_LIST_KEY, startPageUrl).doRedis(jedisPool);
        new PageUrlSaveTask(Constants.JD_PAGE_URL_LIST_KEY_COPY, startPageUrl).doRedis(jedisPool);
        while (true) {
            PageUrlGetTask pageUrlGetTask = new PageUrlGetTask(Constants.JD_PAGE_URL_LIST_KEY);
            pageUrlGetTask.doRedis(jedisPool);
            curPageUrl = pageUrlGetTask.pageUrl;
            log.info("curPageUrl={};", curPageUrl);
            if (Strings.isNullOrEmpty(curPageUrl)) {
                break;
            }
            //webDriverTeam.doTask(new JDShoePageTask(curPageUrl, jedisPool));
            webDriverWorker.doTask(new JDShoePageTask(curPageUrl, jedisPool));
        }
    }

    public void parseAllPageUrl(final String startPageUrl) {
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts()
                .implicitlyWait(20, TimeUnit.SECONDS)
                .setScriptTimeout(20, TimeUnit.SECONDS)
                .pageLoadTimeout(20, TimeUnit.SECONDS);

        List<String> urlList = new ArrayList<>(128);
        urlList.add(startPageUrl);
        String currentPageUrl = startPageUrl;

        int tries = 3;
        while (true) {
            log.info("start currentPageUrl={};urlList.size={};", currentPageUrl, urlList.size());
            webDriver.get(currentPageUrl);
            String nextPageUrl = parseNextPageUrl(webDriver);
            if (currentPageUrl.equals(nextPageUrl)) {
                if (tries <= 0) {
                    log.info("same,exit;currentPageUrl={};nextPageUrl={};", currentPageUrl, nextPageUrl);
                    break;
                } else {
                    tries--;
                }
            } else if (nextPageUrl != null) {
                tries = 3;
                urlList.add(nextPageUrl);
                currentPageUrl = nextPageUrl;
            }
        }
        String fileName = "D:\\projs\\java_projs\\webspider\\docs\\data\\jd_url2.txt";

        CommonUtils.write(fileName, Joiner.on("\n").join(urlList).getBytes());

        log.info("\nstartPageUrl={};\nurlList.size={};\nurlList={};", startPageUrl, urlList.size(), urlList);
        webDriver.quit();
    }

    private String parseNextPageUrl(final WebDriver webDriver) {

        String currentUrl = null;
        try {
            currentUrl = webDriver.getCurrentUrl();
            webDriver.findElement(By.partialLinkText("下一页")).click();

            CommonUtils.sleep((Constants.RANDOM.nextInt(20) + 11) * 1000);

            return webDriver.getCurrentUrl();
        } catch (Exception e) {
            log.error("parse next page url error!currentUrl={};", currentUrl);
            return null;
        }
    }

    public void parseAllShoeDetailUrl() {
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts()
                .implicitlyWait(20, TimeUnit.SECONDS)
                .setScriptTimeout(20, TimeUnit.SECONDS)
                .pageLoadTimeout(20, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            webDriver.quit();
        }));

        List<String> detailUrlList = new ArrayList<>(128);

        String[] pageUrls = {"https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=1&s=1&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=3&s=61&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=5&s=116&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=7&s=171&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=9&s=226&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=11&s=281&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=13&s=336&click=0",
                "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=15&s=391&click=0"};


        for (String pageUrl : pageUrls) {
            detailUrlList.addAll(parseShoeDetailUrl(webDriver, pageUrl));
        }


        log.info("detailUrlList.size={};", detailUrlList.size());

        final String fileName = "D:\\projs\\java_projs\\webspider\\docs\\data\\detail2.txt";

        CommonUtils.write(fileName, Joiner.on('\n').join(detailUrlList).getBytes(Charset.forName("UTF-8")));
    }

    private List<String> parseShoeDetailUrl(final WebDriver webDriver, final String pageUrl) {

        try {
            webDriver.get(pageUrl);
            CommonUtils.sleep((Constants.RANDOM.nextInt(15) + 16) * 1000);
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            Object ret = js.executeScript("scrollBy(0, 800)");
            CommonUtils.sleep((Constants.RANDOM.nextInt(15) + 16) * 1000);

            //WebElement pageElement = webDriver.findElement(By.xpath("//div[@id=\"J_searchWrap\"]"));
            WebElement shoeArea = webDriver.findElement(By.xpath("//div[@id=\"J_goodsList\"]/ul"));

            log.info("ret={};shoeArea={};", ret, shoeArea);

            //List<WebElement> shoeElements = shoeArea.findElements(By.xpath("//a[@target=\"_blank\"]"));
            List<WebElement> shoeElements = shoeArea.findElements(By.xpath("//li[@class=\"gl-item\"]"));

            log.info("shoeElements.size={};", shoeElements.size());

            List<String> detailUrlList = shoeElements.stream().map(shoeElement -> {
                try {
                    //WebElement detailElement = shoeElement.findElement();
                    //WebElement item = shoeElement.findElement(By.xpath("//li[@class=\"gl-item\"]"));
                    //WebElement item = shoeElement.findElement(By.xpath("//div[@class=\"p-img\"]"));
                    //WebElement detailElement = item.findElement(By.xpath("//a[@target=\"_blank\"]"));
                    WebElement detailElement = shoeElement.findElement(By.className("p-img")).findElement(By.tagName("a"));
                    String href = detailElement.getAttribute("href");
                    String title = detailElement.getAttribute("title");
                    log.info("pageUrl={};title={};href={};", pageUrl, title, href);
                    return new StringBuilder().append(title).append("__").append(href).substring(0);
                } catch (Exception e) {
                    log.error("get url error!pageUrl={};", pageUrl, e);
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            log.info("pageUrl={};detailUrlList.size={};", pageUrl, detailUrlList.size());
            return detailUrlList;
        } catch (Exception e) {
            log.error("parse detail url error!pageUrl={};", pageUrl, e);
            return Collections.EMPTY_LIST;
        }
    }


}
