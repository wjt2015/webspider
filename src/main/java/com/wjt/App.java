package com.wjt;

import com.google.common.collect.Lists;
import com.wjt.common.Constants;
import com.wjt.service.PageParseService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Selectable;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    static {
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\chromedriver\\chromedriver_win32_81\\chromedriver.exe");
    }

    public static void main(String[] args) {
        //System.out.println("Hello World!");

        //spiderA();

        //selenium();

        PageParseService pageParseService = new PageParseService();
        pageParseService.parseTeacherByPage();
        pageParseService.close();

    }


    public static void spiderA() {
        Site site = Site.me().setRetryTimes(3).setSleepTime(1000000 * 10);
        String[] startUrls = {"http://my.oschina.net/"};

        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                Selectable url = page.getUrl();
                String rawText = page.getRawText();
                LOGGER.info("page={};rawText={};", page, rawText);
            }

            @Override
            public Site getSite() {

                return site;
            }
        }).setSpiderListeners(Lists.newArrayList(new SpiderListener() {
            @Override
            public void onSuccess(Request request) {
                LOGGER.info("request.url={} success!", request.getUrl());
            }

            @Override
            public void onError(Request request) {
                LOGGER.info("request.url={} error!", request.getUrl());
            }
        }))
                .addPipeline(new Pipeline() {
                    @Override
                    public void process(ResultItems resultItems, Task task) {
                        LOGGER.info("resultItems={};task={};", resultItems, task);
                    }
                }).setScheduler(new Scheduler() {
                    @Override
                    public void push(Request request, Task task) {
                        LOGGER.info("request.url={};task.uuid={};", request.getUrl(), task.getUUID());
                    }

                    @Override
                    public Request poll(Task task) {
                        return null;
                    }
                }).addUrl(startUrls).thread(Constants.EXECUTOR_SERVICE, 5);
        LOGGER.info("spider={};", spider);
        spider.run();
    }

    private static void selenium() {

        // 设置ChromeDriver的路径
        //System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "D:\\soft\\chromedriver\\chromedriver_win32_81\\chromedriver.exe");

        WebDriver webDriver = new ChromeDriver();


        webDriver.get("https://blog.csdn.net/qq_22003641/article/details/79137327");

        String pageSource = webDriver.getPageSource();

        LOGGER.info("pageSource={};", pageSource);

        webDriver.close();
        LOGGER.info("webDriver={};", webDriver);

    }
}
