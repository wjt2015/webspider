package com.wjt.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.wjt.model.PageModel;
import com.wjt.model.TeacherModel;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Time 2020/4/21/4:06
 * @Author jintao.wang
 * @Description
 */
public class PageParseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageParseService.class);

    private String dir = "D:\\projs\\java_projs\\webspider\\docs\\data\\";

    private WebDriver webDriver;

    public PageParseService() {
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
                .setScriptTimeout(10, TimeUnit.SECONDS)
                .pageLoadTimeout(10, TimeUnit.SECONDS);

    }

    public void close() {
        webDriver.quit();
    }

    public void parsePages() {
        final String nextBtnText = "下一个 ›";


    }

    public PageModel parseTeacher(final String pageUrl) {
        webDriver.get(pageUrl);
        List<WebElement> teacherElements = webDriver.findElements(By.className("node__content"));

        List<TeacherModel> teacherModelList = teacherElements.stream().map(teacherElement -> {
            try {
                WebElement img = teacherElement.findElement(By.tagName("img"));
                String alt = img.getAttribute("alt");
                String src = img.getAttribute("src");
                String url = teacherElement.findElement(By.className("node__links")).findElement(By.tagName("a")).getAttribute("href");

                return new TeacherModel(alt, src, url, "");
            } catch (Exception e) {
                LOGGER.info("parse error!pageUrl={};", pageUrl, e);
                return new TeacherModel();
            }

        }).collect(Collectors.toList());

        String nextPageUrl = "";
        try {
            WebElement nextBtn = webDriver.findElement(By.className("pager__item--next")).findElement(By.xpath("//span[@aria-hidden=\"true\"]"));
            nextBtn.click();
            nextPageUrl = webDriver.getCurrentUrl();
        } catch (Exception e) {
            LOGGER.error("find next btn error!pageUrl={};", pageUrl, e);
        }

        return new PageModel(pageUrl, nextPageUrl, teacherModelList);
    }
    public void parseDetail(final String homeUrl){

    }

    public void parseTeacherByPage() {
        final String pageUrl = "http://sti.blcu.edu.cn/zh-hans/core_team";
        PageModel pageModel = parseTeacher(pageUrl);

        LOGGER.info("pageModel={};", pageModel);
    }


    public void parseTeacher() {
        String url = "http://sti.blcu.edu.cn/zh-hans/core_team";
        webDriver.get(url);
        List<WebElement> elements = webDriver.findElements(By.className("node__content"));
        LOGGER.info("elements.size={};elements={};", elements.size(), elements);

/*        List<String> imgs = elements.stream().map(element -> {
            WebElement img = element.findElement(By.tagName("img"));
            return img.getAttribute("src");
        }).collect(Collectors.toList());
        LOGGER.info("imgs.size={};imgs={};", imgs.size(), imgs);*/

        List<TeacherModel> teacherModels = elements.stream().map(element -> {
            WebElement img = element.findElement(By.tagName("img"));
            String src = img.getAttribute("src");
            String alt = img.getAttribute("alt");
            LOGGER.info("alt={};", alt);
            List<WebElement> spans = element.findElements(By.tagName("span"));
            String desc = "";
            if (CollectionUtils.isNotEmpty(spans)) {
                List<String> texts = spans.stream().map(span -> span.getText()).collect(Collectors.toList());
                desc = Joiner.on("").join(texts);
            }

            String homeUrl = element.findElement(By.className("node__links")).findElement(By.tagName("a")).getAttribute("href");

            return new TeacherModel(alt, src, homeUrl, desc);
        }).collect(Collectors.toList());

        LOGGER.info("teacherModels.size={};teacherModels={};", teacherModels.size(), teacherModels);

        WebElement nextBtn = webDriver.findElement(By.className("pager__item--next")).findElement(By.xpath("//span[@aria-hidden=\"true\"]"));

        LOGGER.info("nextBtn={};nextBtn.text={};", nextBtn, nextBtn.getText());

        nextBtn.click();

        String currentUrl = webDriver.getCurrentUrl();

        LOGGER.info("currentUrl={};", currentUrl);
    }

    public void parseData() {
        //System.setProperty("webdriver.chrome.driver", "D:\\soft\\chromedriver\\chromedriver_win32_81\\chromedriver.exe");

/*        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().timeouts()
                .pageLoadTimeout(50, TimeUnit.SECONDS)
                .implicitlyWait(50, TimeUnit.SECONDS)
                .setScriptTimeout(50, TimeUnit.SECONDS);*/

        //String url="https://search.jd.com/Search?keyword=%E9%AB%98%E8%B7%9F%E9%9E%8B%E5%A5%B3&enc=utf-8&suggest=1.def.0.V08--12s0,38s0,97s0&wq=%E9%AB%98%E8%B7%9F%E9%9E%8B&pvid=fa04debf11f048efa15ad274808d98b8";
        String url = "http://sti.blcu.edu.cn/zh-hans/core_team";

        webDriver.get(url);

        String pageSource = webDriver.getPageSource();

/*
        String fileName = "D:\\projs\\java_projs\\webspider\\docs\\data\\t.html";
      try {
            Files.write(Paths.get(fileName), pageSource.getBytes());
        } catch (IOException e) {
            LOGGER.error("write error!fileName={};", fileName, e);
        }*/

        WebElement wrapper = webDriver.findElement(By.id("wrapper"));

        //final String modelClass = "node__content clearfix";
        final String modelClass = "node__content";
        //final String descClassName = "clearfix text-formatted field field--name-body field--type-text-with-summary field--label-hidden field__item";
        final String descClassName = "field__item";
        List<WebElement> elements = wrapper.findElements(By.className(modelClass));
        LOGGER.info("elements={};", elements);
        List<String> textList = elements.stream().map(element -> {
            WebElement descElement = element.findElement(By.className(descClassName));
            if (descElement == null) {
                return "++";
            }
            WebElement p = null;

            try {
                p = descElement.findElement(By.xpath("//span"));

            } catch (Exception e) {
                LOGGER.error("parse error!descElement={};", descElement, e);
            }

            LOGGER.info("p={};", p);
            return (p != null ? p.getText() : "--");
        }).collect(Collectors.toList());

        LOGGER.info("textList={};", textList);

        //LOGGER.info("elements={};", elements);

/*        List<TeacherModel> teacherModels = elements.stream().map(element -> {
            try {

                String src = element.getAttribute("src");
                String alt = element.getAttribute("alt");

                return new TeacherModel(alt,src);
            } catch (Exception e) {
                LOGGER.info("parse error!element={};", element, e);
                return new TeacherModel();
            }
        }).collect(Collectors.toList());
        LOGGER.info("teacherModels={};", teacherModels);*/

        //wrapper.findElement()

        webDriver.quit();
    }
}
