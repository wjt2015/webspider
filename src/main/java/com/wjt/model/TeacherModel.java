package com.wjt.model;

/**
 * @Time 2020/4/21/16:46
 * @Author jintao.wang
 * @Description
 */
public class TeacherModel {

    public String name = "";
    public String src = "";
    public String url = "";
    public String desc;

    public TeacherModel(String name, String src, String url, String desc) {
        this.name = name;
        this.src = src;
        this.url = url;
        this.desc = desc;
    }

    public TeacherModel() {
    }

    @Override
    public String toString() {
        return "TeacherModel{" +
                "name='" + name + '\'' +
                ", src='" + src + '\'' +
                ", url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
