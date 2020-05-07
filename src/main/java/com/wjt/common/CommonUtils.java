package com.wjt.common;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Time 2020/4/23/1:23
 * @Author jintao.wang
 * @Description
 */
@Slf4j
public class CommonUtils {

    /**
     * //下拉到页面底部
     * @param webDriver
     */
    public static void scrollToBottom(final WebDriver webDriver) {
        //下拉到页面底部
        //((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");

        //上拉到页面顶端
        //((JavascriptExecutor) webDriver).executeScript("window.scrollTo(document.body.scrollHeight,0)");


        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    public static void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            log.error("Interrupted while sleeping!", e);
        }
    }

    public static boolean write(String fileName, byte[] content) {

        try {
            Files.write(Paths.get(fileName), content);
            return true;
        } catch (IOException e) {
            log.error("write error!fileName={};", fileName);
            return false;
        }
    }

}
