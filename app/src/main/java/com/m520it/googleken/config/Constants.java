package com.m520it.googleken.config;

import com.m520it.googleken.utils.LogUtils;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:36
 * @desc 常量存放类
 */
public class Constants {
    //开发的时候打印log的开发是是打开的
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final long SAFETIMER = 6 * 1000 * 60;

    public static final class URLS {
        //"http://192.168.33.122:8080/GooglePlayServer/home?index=0"
        public static final String BASEURL = "http://192.168.33.122:8080/GooglePlayServer/";
        //图片下载路径
        public static  final String LOADIMAGER=BASEURL+"image?name=";
        //APK下载的地址
        public static final String DOWNLOADAPP=BASEURL+"download?";
    }
}
