package com.wjt;

import com.google.common.collect.Lists;
import com.wjt.common.Constants;
import com.wjt.config.AppConfig;
import com.wjt.model.WebDriverTeam;
import com.wjt.service.JDShoeService;
import com.wjt.service.PageParseService;
import org.omg.SendingContext.RunTime;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    static {
        System.setProperty("webdriver.chrome.driver", Constants.CHROME_DRIVER_BIN);
    }

    public static void main(String[] args) {
        //jd();
        //seleniumB();
        //seleniumC();
        //seleniumD();
        seleniumF();
    }

    public static void jd() {

        final long start = System.currentTimeMillis();
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        JDShoeService jdShoeService = applicationContext.getBean(JDShoeService.class);
        //WebDriverTeam webDriverTeam = applicationContext.getBean(WebDriverTeam.class);
        final String pageUrl = "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&suggest=1.his.0.0&wq=&pvid=3fdd052657f94863b6274f22ec46b659";
        jdShoeService.parsePageUrls(pageUrl);
        LOGGER.info("complete!elapsed={}ms;", (System.currentTimeMillis() - start));
        //webDriverTeam.quit();
    }

    public static void seleniumB() {
        //System.out.println("Hello World!");
        //spiderA();
        //selenium();
        PageParseService pageParseService = new PageParseService();
        pageParseService.parseTeacherByPage();
        pageParseService.close();
    }

    public static void seleniumC() {

        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);
        String pageUrl = "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&suggest=1.his.0.0&wq=&pvid=3fdd052657f94863b6274f22ec46b659";
        webDriver.get(pageUrl);
        List<WebElement> elements = webDriver.findElements(By.tagName("div"));
        LOGGER.info("pageUrl={};elements.size={};", pageUrl, elements.size());

        pageUrl = "https://item.jd.com/34621927189.html";
        webDriver.get(pageUrl);
        elements = webDriver.findElements(By.tagName("div"));
        LOGGER.info("pageUrl={};elements.size={};", pageUrl, elements.size());

        pageUrl = "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=5&s=116&click=0";
        webDriver.get(pageUrl);
        elements = webDriver.findElements(By.tagName("div"));
        LOGGER.info("pageUrl={};elements.size={};", pageUrl, elements.size());

        webDriver.quit();

    }

    public static void seleniumD() {
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);
        String pageUrl = "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=197&s=5868&click=0";

        webDriver.get(pageUrl);
        webDriver.findElement(By.partialLinkText("下一页")).click();
        String urlA = webDriver.getCurrentUrl();

        LOGGER.info("urlA={};", urlA);

        webDriver.get(urlA);
        webDriver.findElement(By.partialLinkText("下一页")).click();
        String urlB = webDriver.getCurrentUrl();
        LOGGER.info("urlB={};", urlB);

        webDriver.get(urlB);
        webDriver.findElement(By.partialLinkText("下一页")).click();
        String urlC = webDriver.getCurrentUrl();
        LOGGER.info("urlC={};", urlC);

        webDriver.quit();
    }

    public static void seleniumE() {
        final String pageUrl = "https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=1.his.0.0&page=1&s=1&click=0";
        new JDShoeService().parseAllPageUrl(pageUrl);
    }

    public static void seleniumF() {
        new JDShoeService().parseAllShoeDetailUrl();
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
