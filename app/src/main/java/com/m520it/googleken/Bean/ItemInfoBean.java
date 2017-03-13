package com.m520it.googleken.Bean;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  9:52
 * @desc ItemInfoBean 可以公用
 */
public class ItemInfoBean {
    /**
     * id : 1525489
     * name : 小码哥程序员
     * packageName : com.m520it.www
     * iconUrl : app/com.m520it.www/icon.jpg
     * stars : 5
     * size : 91767
     * downloadUrl : app/com.m520it.www/com.m520it.www.apk
     * des : 产品介绍：google市场app测试。
     */
    public String des;
    public String downloadUrl;
    public String iconUrl;
    public int id;
    public String name;
    public String packageName;
    public long size;
    public float stars;
    public String downloadNum;
    public String version;
    public String date;
    public String author;
    public List<String> screen;
    public List<SafeInfoBean> safe;

    public  class SafeInfoBean{
        public String safeUrl;
        public String safeDesUrl;
        public String safeDes;
        public String safeDesColor;

    }

    @Override
    public String toString() {
        return "ItemInfoBean{" +
                "des='" + des + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", size=" + size +
                ", stars=" + stars +
                ", downloadNum='" + downloadNum + '\'' +
                ", version='" + version + '\'' +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", screen=" + screen +
                ", safe=" + safe +
                '}';
    }
}
