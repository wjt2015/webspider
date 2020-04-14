package com.wjt;

import com.wjt.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;

/**
 * Hello world!
 */
public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        //System.out.println("Hello World!");

        spiderA();
    }


    public static void spiderA() {

        Spider spider = Spider.create(new PageProcessor() {
            @Override
            public void process(Page page) {
                LOGGER.info("page={};", page);
            }

            @Override
            public Site getSite() {

                return null;
            }
        }).addUrl("http://git.intra.weibo.com/im/group_chat/wikis/%E7%BE%A4%E8%81%8A%E9%A1%B9%E7%9B%AE%E6%80%BB%E7%BB%93")
                .addPipeline(new Pipeline() {
                    @Override
                    public void process(ResultItems resultItems, Task task) {

                    }
                }).setScheduler(new Scheduler() {
                    @Override
                    public void push(Request request, Task task) {

                    }

                    @Override
                    public Request poll(Task task) {
                        return null;
                    }
                }).thread(Constants.EXECUTOR_SERVICE, 5);
        LOGGER.info("spider={};", spider);
        spider.run();

    }
}
