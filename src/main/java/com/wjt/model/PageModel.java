package com.wjt.model;

import java.util.List;

/**
 * @Time 2020/4/22/0:04
 * @Author jintao.wang
 * @Description
 */
public class PageModel {
    public String pageUrl;
    public String nextPageUrl;
    public List<TeacherModel> teacherModelList;

    public PageModel() {
    }

    public PageModel(String pageUrl, String nextPageUrl, List<TeacherModel> teacherModelList) {
        this.pageUrl = pageUrl;
        this.nextPageUrl = nextPageUrl;
        this.teacherModelList = teacherModelList;
    }

    @Override
    public String toString() {
        return "PageModel{" +
                "pageUrl='" + pageUrl + '\'' +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", teacherModelList=" + teacherModelList +
                '}';
    }
}
