package com.wjt.common;

import lombok.extern.slf4j.Slf4j;

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
