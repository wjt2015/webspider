package com.wjt.config;

import com.wjt.dao.JunjinArticleMapper;
import com.wjt.model.JunjinArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBConfig.class})
public class DBConfigTest {

    @Resource
    private JunjinArticleMapper junjinArticleMapper;


    @Test
    public void save() {
        JunjinArticleEntity junjinArticleEntity = new JunjinArticleEntity();
        junjinArticleEntity.title = "【源码面经】Java源码系列-LinkedHashMap";
        junjinArticleEntity.url = "https://juejin.im/post/5eae84daf265da7bf7328e25";
        String content = "LinkedHashMap在Map的基础上进行了扩展，提供了按序访问的能力。这个顺序通过accessOrder控制，可以是结点的插入顺序，也可以是结点的访问时间顺序。LinkedHashMap还提供了removeEldestEntry方法，可以用来删除最老访问结点。通过accessOrder和removeEldestEntry可以用来实现LRU缓存。如图所示，LinkedHashMap实现顺序访问的方法比较简单，在HashMap实现之外，还维护了一个双向链表。每当插入结点时，不仅要在Map中维护，还需要在链表中进行维护。HashMap中的put, get等方法都提供了一些钩子方法，如afterNodeAccess、afterNodeInsertion和afterNodeRemoval等。通过这些方法，LinkedHashMap可以对这些结点进行一些特性化的维护。当遍历LinkedHashMap时通过遍历链表代替遍历Map中的各个槽，从而实现按序访问。LinkedHashMap如何实现有序的LinkedHashMap在HashMap的基础上，还将每个key-value对应的Node维护在了一个额外的双向链表中。LinkedHashMap通过accessOrder可以支持按插入的顺序访问，或者按遍历的顺序访问accessOrder遍历的时候，通过便利双向链表代替遍历map的每个槽，来实现顺序访问。如何用LinkedHashMap实现LRU首先分析LRU算法有哪些特性在LinkedHashMap保证结点有序的情况下，通过设置accessOrder为true，采用按遍历顺序维护结点。;";
        junjinArticleEntity.summary = content.substring(0,100);
        int ret = junjinArticleMapper.insertSelective(junjinArticleEntity);

        log.info("ret={};", ret);

    }

}