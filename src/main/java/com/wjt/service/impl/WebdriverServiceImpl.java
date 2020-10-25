package com.wjt.service.impl;

import com.wjt.common.Constants;
import com.wjt.dao.GuanghuaMapper;
import com.wjt.service.WebdriverService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import redis.clients.jedis.MyJedisPool;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class WebdriverServiceImpl implements WebdriverService {

    @Resource(name = "chromeDriver")
    private WebDriver webDriver;

    @Resource(name = "chromeDriverForDetail")
    private WebDriver chromeDriverForDetail;

    @Resource
    private MyJedisPool myJedisPool;

    @Resource
    private GuanghuaMapper guanghuaMapper;


    @Override
    public void saveRiyueguanghuaBBS(String url) {
        final ChromeDriver chromeDriver=(ChromeDriver)webDriver;

        try {
            chromeDriver.get(url);
            chromeDriver.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            /**
             * //*[@id="b-n"]/a
             *
             * /html/body/div[1]/div/div/div[4]/a
             */
            WebElement element = chromeDriver.findElement(By.xpath("//*[@id=\"b-n\"]/a"));
            log.info("element={};text={};href={};",element,element.getText(),element.getAttribute("href"));

            String xpath="//a[@class=\"b-t\"]";
            List<WebElement> elements = chromeDriver.findElements(By.xpath(xpath));

            Actions actions=new Actions(chromeDriver);
            for (WebElement webElement:elements){
                //WebElement element1 = webElement.findElement(By.xpath("//a[@class=\"b-t\"]"));
                WebElement element1 = webElement;
                log.info("text={};href={};",element1.getText(),element1.getAttribute("href"));

                actions.click(element1);
                chromeDriver.executeScript(Constants.SCROLL_BOTTOM_JS);
                WebElement articleElement = chromeDriver.findElement(By.xpath("//*[@id=\"p-l\"]/div[1]"));

                log.info("articleElement_text={};",articleElement.getText());

                chromeDriver.navigate().back();

            }
        }catch (Exception e){
            log.error("chromeDriver error!",e);
        }finally {
            chromeDriver.quit();
        }
    }





}
