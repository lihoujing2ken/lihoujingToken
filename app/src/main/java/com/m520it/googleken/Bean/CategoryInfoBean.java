package com.m520it.googleken.Bean;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  19:47
 * @desc CategoryFragment界面 的Bean 数据
 */
public class CategoryInfoBean {
    public String name1;
    public String name2;
    public String name3;
    public String url1;
    public String url2;
    public String url3;

    public String title;
    public boolean isTitle;

    @Override
    public String toString() {
        return "CategoryInfoBean{" +
                "name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", title='" + title + '\'' +
                ", isTitle=" + isTitle +
                '}';
    }
}
