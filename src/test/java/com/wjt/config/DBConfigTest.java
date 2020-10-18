package com.wjt.config;

import com.wjt.dao.JunjinArticleMapper;
import com.wjt.model.JunjinArticleEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
        junjinArticleEntity.summary = content.substring(0, 100);
        int ret = junjinArticleMapper.insertSelective(junjinArticleEntity);

        log.info("ret={};", ret);
    }

    @Test
    public void select() {
        long id = 1L;
        JunjinArticleEntity junjinArticleEntity = junjinArticleMapper.selectByPrimaryKey(id);
        log.info("junjinArticleEntity={};", junjinArticleEntity);

        id = 5L;
        junjinArticleEntity = junjinArticleMapper.selectByPrimaryKey(id);
        log.info("junjinArticleEntity={};", junjinArticleEntity);

    }

    @Test
    public void update() {
        long id = 1L;
        JunjinArticleEntity junjinArticleEntity = junjinArticleMapper.selectByPrimaryKey(id);
        log.info("junjinArticleEntity={};", junjinArticleEntity);

        junjinArticleEntity.setSummary("java ssm hashmap");

        int update = junjinArticleMapper.updateByPrimaryKeySelective(junjinArticleEntity);

        log.info("update={};", update);

    }

    /**
     * 参考:
     * (https://www.jianshu.com/p/c0acbd18794c);
     * (https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html);
     * (https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html);
     * @throws ClassNotFoundException
     */
    @Test
    public void jdbc() {

        //final String url="jdbc:mysql://127.0.0.1:3306/web_train?useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&connectTimeout=1000&socketTimeout=1000&autoReconnect=true";
        final String url="jdbc:mysql://127.0.0.1:3306/web_train";

        final Properties properties=new Properties();
        //useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&connectTimeout=1000&socketTimeout=1000&autoReconnect=true
        properties.setProperty("username","root");
        properties.setProperty("password","linux2014");

        final String sql="insert into juejin_article(title,url,summary) values(?,?,?)";
        final String driverClassName="com.mysql.cj.jdbc.Driver";

        //加载mysql驱动;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("load driver class error!",e);
        }
        //建立mysql连接;
        try (Connection conn = DriverManager.getConnection(url, properties)){
            conn.setAutoCommit(true);

            //插入记录;
            new MySqlConnTask(){
                @Override
                public Object doTask(Connection conn, Object obj) {

                    try (PreparedStatement preparedStatement = conn.prepareStatement(sql, new int[]{1, 2, 3})){

                        preparedStatement.setString(1,"RocketMQ架构");
                        preparedStatement.setString(2,"https://juejin.im/post/6844903830874750990");
                        preparedStatement.setString(3,"Apache RocketMQ是一个分布式消息和流处理平台，具有低延迟，高性能和高可靠性，亿万级容量和灵活的可扩展性。它由四部分组成：名称服务器，代理服务器，生产者和消费者。它们中的每一个都可以水平扩展，而不会出现单点故障。如上图所示。");

                        int update = preparedStatement.executeUpdate();
                        ResultSet resultSet = preparedStatement.getResultSet();

                        log.info("update={};resultSet={};",update,resultSet);
                    } catch (SQLException e) {
                        log.error("preparedStatement error!",e);
                    }

                    return null;
                }
            }.doTask(conn,null);


        }catch (Exception e){
            log.error("db conn error!",e);
        }finally {

        }
    }

    /**
     * 利用mysql连接执行任务;
     */
    static interface MySqlConnTask{
        Object doTask(final Connection conn,Object obj);
    }



    /**
     * 参考(https://ke.qq.com/webcourse/index.html#cid=2024404&term_id=102125540&taid=8106965521589204);
     */
    @Test
    public void mybatisProxy(){

        Proxy proxy;



    }


}


