package com.wjt.config;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Test;
import org.junit.runner.RunWith;
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

}