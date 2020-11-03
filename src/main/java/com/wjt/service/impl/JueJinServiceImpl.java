package com.wjt.service.impl;

import com.wjt.common.CommonUtils;
import com.wjt.common.Constants;
import com.wjt.dao.JuejinArticleMapper;
import com.wjt.model.JuejinArticleEntity;
import com.wjt.service.ExistChecker;
import com.wjt.service.JueJinService;
import com.wjt.task.MyJedisTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.MyJedis;
import redis.clients.jedis.MyJedisPool;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JueJinServiceImpl implements JueJinService {

    @Resource
    private JuejinArticleMapper juejinArticleMapper;

    @Resource
    private JedisPool jedisPool;

    @Resource
    private MyJedisPool myJedisPool;

    @Resource
    private ExistChecker existChecker;

    private final String TO_USE_URL_LIST = "to_use_url_list";

    @Override
    public void getJuejinArticles(String startPageUrl) {
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS)
                .pageLoadTimeout(20, TimeUnit.SECONDS)
                .setScriptTimeout(20, TimeUnit.SECONDS);

        new MyJedisTask() {
            @Override
            public Object doJedisTask(MyJedis myJedis, Object o) {
                myJedis.lpush(TO_USE_URL_LIST, startPageUrl);
                return null;
            }
        }.doTask(myJedisPool,null);

        String currentUrl;
        int n = 0;
        final long startTime = System.currentTimeMillis();

        final MyJedisTask myJedisTask=new MyJedisTask() {
            @Override
            public Object doJedisTask(MyJedis myJedis, Object o) {
                return myJedis.rpop(TO_USE_URL_LIST);
            }
        };

        while ((currentUrl = (String) (myJedisTask.doTask(myJedisPool,null))) != null && currentUrl.length() >= 5) {
            log.info("start to get!currentUrl={};", currentUrl);
            n++;
            final long start = System.currentTimeMillis();
            try {
                webDriver.get(currentUrl);
                CommonUtils.sleep((Constants.RANDOM.nextInt(16) + 17) * 1000);
                getCurPage(webDriver);
            } catch (Exception e) {
                log.error("An exception occurs while getting page content!url={};", currentUrl, e);
            }
            log.info("complete current page;elapsed={}ms;currentUrl={};", (System.currentTimeMillis() - start), currentUrl);

        }
        webDriver.quit();
        log.info("complete all pages;elapsed={}ms;n={};", (System.currentTimeMillis() - startTime), n);
    }

    @Override
    public void saveRelationship(String url) {
        final ChromeDriver webDriver=new ChromeDriver();
        webDriver.manage().timeouts().pageLoadTimeout(2,TimeUnit.SECONDS)
                .setScriptTimeout(2,TimeUnit.SECONDS)
        .implicitlyWait(2,TimeUnit.SECONDS);

        try {
            webDriver.get(url);

            //webDriver.manage().window().setPosition(new Point(1000,1000));

            //下拉页面;
            //webDriver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            webDriver.executeScript("window.scrollTo(0, document.body.scrollHeight/2);");

            CommonUtils.sleep(3000);

            List<WebElement> webElementList = webDriver.findElements(By.tagName("a"));
            if(CollectionUtils.isNotEmpty(webElementList)){
                webElementList.forEach(webElement -> {
                    String href = webElement.getAttribute("href");
                    if(href!=null){
                        log.info("href={};",href);
                    }

                });
            }
        }catch (Exception e){
            log.error("webDriver error!",e);
        }finally {
            webDriver.quit();

        }
    }




    private void getCurPage(WebDriver webDriver) throws Exception {
        String currentUrl = webDriver.getCurrentUrl();
        WebElement articleElement = null;
        articleElement = webDriver.findElement(By.xpath("//*[@id=\"juejin\"]/div[2]/main/div/div[1]/article"));
        WebElement titleElent = articleElement.findElement(By.xpath("h1[@class=\"article-title\"]"));
        String title = titleElent.getText();

        List<WebElement> contentElements = articleElement.findElements(By.tagName("p"));

        String content = contentElements.stream().map(contentElement -> contentElement.getText()).collect(Collectors.joining());

        log.info("title={}\ncurrentUrl={}\ncontent.size={};content={};",
                title, currentUrl, content.length(), content);

        //save db;
        JuejinArticleEntity juejinArticleEntity =new JuejinArticleEntity();
        juejinArticleEntity.title=title;
        juejinArticleEntity.url=currentUrl;
        juejinArticleEntity.summary=content.substring(0,230);
        juejinArticleMapper.insertSelective(juejinArticleEntity);

        saveRecommendUrls(webDriver);

    }


    private void saveRecommendUrls(WebDriver webDriver) {

        //CommonUtils.sleep((Constants.RANDOM.nextInt(15) + 16) * 1000);
        final String currentUrl = webDriver.getCurrentUrl();
        for (int i = 0, n = 3; i < n; i++) {
            CommonUtils.scrollToBottom(webDriver);
            CommonUtils.sleep((Constants.RANDOM.nextInt(15) + 17) * 1000);
        }

        WebElement urlsElement = webDriver.findElement(By.xpath("//*[@id=\"juejin\"]/div[2]/main/div/div[2]/ul"));

        List<WebElement> elements = urlsElement.findElements(By.xpath("//li/div/div/a"));
        log.info("currentUrl={};elements.size={};", currentUrl, elements.size());

        List<String> recommendUrls = elements.stream().map(element -> element.getAttribute("href")).collect(Collectors.toList());

        log.info("before;recommendUrls.size={};recommendUrls={};", recommendUrls.size(), recommendUrls);

        recommendUrls.removeAll(existChecker.exist(recommendUrls));

        log.info("after;recommendUrls.size={};recommendUrls={};", recommendUrls.size(), recommendUrls);

        if (CollectionUtils.isNotEmpty(recommendUrls)) {
            new MyJedisTask(){
                @Override
                public Object doJedisTask(MyJedis myJedis, Object o) {
                    myJedis.lpush(TO_USE_URL_LIST, recommendUrls.toArray(new String[recommendUrls.size()]));
                    return null;
                }
            }.doTask(myJedisPool,null);
        }

    }

}
