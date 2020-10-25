package com.wjt.config;

import com.wjt.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpiderConfig.class})
public class SpiderConfigTest {

    @Resource
    private OkHttpClient okHttpClient;
    @Resource(name = "chromeDriver")
    private WebDriver webDriver;

    @Test
    public void okHttp() {
        final long start = System.currentTimeMillis();
        //final String url="https://ke.qq.com/course/464284?taid=4091518990620060#term_id=100555631";
        final String url = "http://cdn.yishilvyou.com/upload/userfile/68/page/image/2020-10-15/727902f1017640afb2171767f84b9e80.jpg";
        final String fileDir = "/Users/jintao9/linux2014/test/request_data/";

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        boolean successful = false;
        try (Response response = okHttpClient.newCall(request).execute()) {
            successful = response.isSuccessful();
            ResponseBody responseBody = response.body();
            final BufferedInputStream bufferedInputStream = new BufferedInputStream(responseBody.byteStream());

            Files.copy(bufferedInputStream, Paths.get(fileDir + "a.jpg"));

        } catch (Exception e) {
            log.error("okHttp request error!", e);
        } finally {
            log.info("finish!successful={};elapsed={}ms;", successful, (System.currentTimeMillis() - start));
        }
    }

    /**
     * 有道翻译(http://fanyi.youdao.com/)抓包;
     */
    @Test
    public void tanslateByWebDriver() {
        webDriver.get("http://fanyi.youdao.com/");
        final WebElement wordWnd = webDriver.findElement(By.xpath("//*[@id=\"inputOriginal\"]"));
        final WebElement translatedWnd = webDriver.findElement(By.xpath("//*[@id=\"transTarget\"]"));
        wordWnd.sendKeys("dean");
        //休息,等待http返回;
        CommonUtils.sleep(3000);
        String text = translatedWnd.getText();
        log.info("text={};", text);

    }


    @Test
    public void tanslate() {

        //http://fanyi.youdao.com/translate_o?smartresult=dict&smartresult=rule
        //final String url = "http://fanyi.youdao.com/";
        final String url = "http://fanyi.youdao.com/translate_o?smartresult=dict&smartresult=rule";

        String word = "connection";

        word = "porn";


        Request.Builder builder = new Request.Builder()
                .url(url).post(formBody(word));
        addHeaders(builder);

        final Request request = builder.build();

        try (Response response = okHttpClient.newCall(request).execute();) {
            boolean successful = response.isSuccessful();
            ResponseBody responseBody = response.body();
            String string = responseBody.string();
            log.info("successful={};string={};", successful, string);

        } catch (Exception e) {
            log.error("okHttpClient error!", e);
        }

    }

    private static Request.Builder addHeaders(Request.Builder builder) {
        builder.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                .addHeader("Referer", "http://fanyi.youdao.com/")
                //.addHeader("Cookie", "_ga=GA1.2.838394958.1597165036; OUTFOX_SEARCH_USER_ID_NCOO=1436495838.1748514; OUTFOX_SEARCH_USER_ID=\"-182909040@10.108.160.19\"; JSESSIONID=aaafLC1qsUi2dgsnJGhvx; ___rl__test__cookies=1603619967473");
                .addHeader("Cookie", "_ga=GA1.2.838394958.1597165036; OUTFOX_SEARCH_USER_ID_NCOO=1436495838.1748514; OUTFOX_SEARCH_USER_ID=\"-182909040@10.108.160.19\"; JSESSIONID=aaafLC1qsUi2dgsnJGhvx; ___rl__test__cookies=1603650173457");
        return builder;
    }


    private static FormBody formBody(final String word) {
        FormBody formBody = new FormBody.Builder()
                .add("i", word)
                .add("from", "AUTO")
                .add("to", "AUTO")
                .add("smartresult", "dict")
                .add("client", "fanyideskweb")
                .add("salt", "16036199674780")
                .add("sign", "5287300951e040d6dabb6feecee139a2")
                .add("lts", "1603619967478")
                .add("bv", "5721905c2bddb057fe043434fc2f8972")
                .add("doctype", "json")
                .add("version", "2.1")
                .add("keyfrom", "fanyi.web")
                .add("action", "FY_BY_REALTlME")
                .build();
        return formBody;

    }


}