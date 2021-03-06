package com.wjt.model;

import lombok.Data;

import java.util.Date;

/**
 create table if not exists article(
 id bigint not null auto_increment comment '自增主键id',
 create_time timestamp default current_timestamp comment '记录的创建时间',
 update_time timestamp default current_timestamp on update current_timestamp comment '记录的更新时间',
 create_user varchar(256) not null default '' comment '记录创作者名字',
 update_user varchar(256) not null default '' comment '记录更新者名字',
 title varchar(256) not null default '' comment '文章标题',
 url varchar(512) not null default '' comment '文章链接',
 summary varchar(512) not null default '' comment '文章概要',
 primary key(id)
 )default character set='utf8' comment='技术文章数据表' engine=innodb;


 */

@Data
public class JuejinArticleEntity {
    public Long id;

    public Date createTime;

    public Date updateTime;

    public String createUser;

    public String updateUser;

    public String title;

    public String url;

    public String summary;

    public JuejinArticleEntity(Long id, Date createTime, Date updateTime, String createUser, String updateUser, String title, String url, String summary) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.title = title;
        this.url = url;
        this.summary = summary;
    }

    public JuejinArticleEntity() {
        super();
    }
    
}