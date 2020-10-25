package com.wjt.service.impl;

import com.wjt.common.CommonUtils;
import com.wjt.common.Constants;
import com.wjt.dao.GuanghuaMapper;
import com.wjt.service.WebdriverService;
import com.wjt.task.MyJedisTask;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Service;
import redis.clients.jedis.MyJedis;
import redis.clients.jedis.MyJedisPool;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try {
            saveRiyueguanghuaBBSC(url);
        } catch (Exception e) {
            log.error("webDriver error!", e);
        } finally {
            webDriver.quit();
        }
    }


    private void saveRiyueguanghuaBBSA(String url) {
        final ChromeDriver chromeDriver = (ChromeDriver) webDriver;

        try {
            chromeDriver.get(url);
            chromeDriver.executeScript(Constants.SCROLL_BOTTOM_JS);
            /**
             * //*[@id="b-n"]/a
             *
             * /html/body/div[1]/div/div/div[4]/a
             */
            WebElement element = chromeDriver.findElement(By.xpath("//*[@id=\"b-n\"]/a"));
            log.info("element={};text={};href={};", element, element.getText(), element.getAttribute("href"));

            String xpath = "//a[@class=\"b-t\"]";
            List<WebElement> elements = chromeDriver.findElements(By.xpath(xpath));

            Actions actions = new Actions(chromeDriver);
            for (WebElement webElement : elements) {
                //WebElement element1 = webElement.findElement(By.xpath("//a[@class=\"b-t\"]"));
                WebElement element1 = webElement;
                log.info("text={};href={};", element1.getText(), element1.getAttribute("href"));

                actions.click(element1);
                chromeDriver.executeScript(Constants.SCROLL_BOTTOM_JS);
                WebElement articleElement = chromeDriver.findElement(By.xpath("//*[@id=\"p-l\"]/div[1]"));

                log.info("articleElement_text={};", articleElement.getText());

                chromeDriver.navigate().back();

            }
        } catch (Exception e) {
            log.error("chromeDriver error!", e);
        } finally {
            chromeDriver.quit();
        }
    }

    private static final String PAGE_URL_LIST = "page_url_list";
    private static final String PAGE_URL_SET = "page_url_set";
    private static final String ITEM_URL_LIST = "riyueguanghua";

    private void saveRiyueguanghuaBBSC(String url) {
        //保存初始链接;
        new MyJedisTask() {
            @Override
            protected Object doJedisTask(MyJedis myJedis, Object o) {
                myJedis.lpush(PAGE_URL_LIST, url);
                myJedis.sadd(PAGE_URL_SET, url);
                return null;
            }
        }.doTask(myJedisPool, null);

        while (true) {
            //取出本页链接;
            Object o = new MyJedisTask() {
                @Override
                protected Object doJedisTask(MyJedis myJedis, Object o) {
                    return myJedis.rpop(PAGE_URL_LIST);
                }
            }.doTask(myJedisPool, null);
            log.info("o={};", o);
            if (o == null || !(o instanceof String)) {
                break;
            }
            final String pageUrl = (String) o;
            //保存本页条目;
            saveRiyueguanghuaBBSB(pageUrl);
        }

    }


    private void saveRiyueguanghuaBBSB(String url) {
        final ChromeDriver chromeDriver = (ChromeDriver) webDriver;
        //li class="bdr-t p-u"
        //final String itemXpath = "//li[@class=\"bdr-t p-u\"]";
        final String itemXpath = "//*[@id=\"b-p\"]/li";

        try {
            chromeDriver.get(url);
            chromeDriver.executeScript(Constants.SCROLL_BOTTOM_JS);

            //上一页;
            WebElement prevElement = chromeDriver.findElement(By.xpath("//*[@id=\"b-n\"]/a[1]"));
            String prevText = prevElement.getText();
            String prevHref = prevElement.getAttribute("href");
            log.info("prevText={};prevHref={};", prevText, prevHref);
            if (prevHref != null) {
                new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        myJedis.lpush(PAGE_URL_LIST, prevHref);
                        myJedis.sadd(PAGE_URL_SET, prevHref);
                        log.info("保存上一页的page_url;prevHref={};", prevHref);
                        return null;
                    }
                }.doTask(myJedisPool, null);
            }

            List<WebElement> itemList = chromeDriver.findElements(By.xpath(itemXpath));
            log.info("itemList.size={};", itemList.size());
            for (WebElement item : itemList) {
                //log.info("item_text={};", item.getText());
                WebElement hrefElement = item.findElement(By.className("b-t"));
                String href = hrefElement.getAttribute("href");
                String text = hrefElement.getText();
                log.info("text={};href={};", text, href);
                if (needSave(text)) {
                    new MyJedisTask() {
                        @Override
                        protected Object doJedisTask(MyJedis myJedis, Object o) {
                            myJedis.hset(ITEM_URL_LIST, text, href);
                            return null;
                        }
                    }.doTask(myJedisPool, null);
                }
            }
        } catch (Exception e) {
            log.error("chrome driver error!url={};", url, e);
        } finally {
            //chromeDriver.quit();
        }
    }

    private static final Pattern PATTERN = Pattern.compile(".*男.*征.*");

    private boolean needSave(final String text) {
        if (text == null) {
            return false;
        }
        final Matcher matcher = PATTERN.matcher(text);
        if (matcher.matches()) {
            log.info("pattern_text={};", text);
            return false;
        }
        if (text.contains("Re:")) {
            return false;
        }

        return (text != null && !text.contains("主题") && !text.contains("鹊.桥.版.规 ≌ 文.贴.定.位") && !text.contains("撤牌"));
    }


    @Override
    public void saveBingmayongBBS(String url) {
        try {
            //保存本页url;
            new MyJedisTask() {
                @Override
                protected Object doJedisTask(MyJedis myJedis, Object o) {
                    myJedis.lpush(BINGMAYONG_URL_LIST, url);
                    myJedis.sadd(BINGMAYONG_URL_SET, url);
                    return null;
                }
            }.doTask(myJedisPool, null);

            while (true) {
                //逐页查询;
                Object o = new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        return myJedis.rpop(BINGMAYONG_URL_LIST);
                    }
                }.doTask(myJedisPool, null);
                log.info("o={};", o);
                if (o == null || !(o instanceof String)) {
                    break;
                }

                saveBingmayongBBSOnePage((String) o);
            }

        } catch (Exception e) {
            log.error("webDriver error!url={};", url, e);
        } finally {
            webDriver.quit();
        }
    }


    private static final String BINGMAYONG_URL_LIST = "bingmayong_url_list";
    private static final String BINGMAYONG_URL_SET = "bingmayong_url_set";
    private static final String BINGMAYONG_FRIENDS = "bingmayong_friends";

    private void saveBingmayongBBSOnePage(String url) {

        final ChromeDriver chromeDriver = (ChromeDriver) webDriver;
        try {
            chromeDriver.get(url);
            chromeDriver.executeScript(Constants.SCROLL_BOTTOM_JS);
            log.info("load_page_finish!url={};", url);

            //保存上一页url;/html/body/table[2]/tbody/tr[2]/td[2]/a[5]
            final WebElement prevElement = chromeDriver.findElement(By.xpath("//table[2]/tbody/tr[2]/td[2]/a[5]"));
            log.info("prevElement={};", prevElement);
            String prevText = prevElement.getText();
            String prevHref = prevElement.getAttribute("href");
            log.info("prevText={};prevHref={};", prevText, prevHref);
            if (prevHref != null) {
                new MyJedisTask() {
                    @Override
                    protected Object doJedisTask(MyJedis myJedis, Object o) {
                        myJedis.sadd(BINGMAYONG_URL_SET, prevHref);
                        myJedis.lpush(BINGMAYONG_URL_LIST, prevHref);
                        return null;
                    }
                }.doTask(myJedisPool, null);
            }

            //保存每个条目;
            List<WebElement> itemList = chromeDriver.findElements(By.xpath("//table[1]/tbody/tr[4]/td/table/tbody/tr"));
            log.info("itemList.size={};", itemList.size());

            for (WebElement item : itemList) {
                WebElement element = item.findElement(By.xpath("//td[5]/a"));
                String text = element.getText();
                String href = element.getAttribute("href");
                log.info("text={};href={};", text, href);
                if (needSave(text)) {
                    new MyJedisTask() {
                        @Override
                        protected Object doJedisTask(MyJedis myJedis, Object o) {
                            myJedis.hset(BINGMAYONG_FRIENDS, text, href);
                            return null;
                        }
                    }.doTask(myJedisPool, null);
                }
            }


        } catch (Exception e) {

        }

    }

}
