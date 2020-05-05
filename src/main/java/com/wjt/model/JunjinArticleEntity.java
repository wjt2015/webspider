package com.wjt.model;

import java.util.Date;

public class JunjinArticleEntity {
    public Long id;

    public Date createTime;

    public Date updateTime;

    public String createUser;

    public String updateUser;

    public String title;

    public String url;

    public String summary;

    public JunjinArticleEntity(Long id, Date createTime, Date updateTime, String createUser, String updateUser, String title, String url, String summary) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.title = title;
        this.url = url;
        this.summary = summary;
    }

    public JunjinArticleEntity() {
        super();
    }
    
}