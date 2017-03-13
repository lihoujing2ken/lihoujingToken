package com.m520it.googleken.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;

import com.m520it.googleken.application.MyApplication;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:17
 * @desc 和UI相关的工具类
 */
public class UIUtils {
    /*---------得到上下文--------*/
    public static Context getContext() {
        return MyApplication.getContext();
    }

    /*---------得到资源管理器--------*/
    public static Resources getResource() {
        return getContext().getResources();
    }

    /*---------方便获得values下面的string.xml里面的String--------*/
    public static String getString(int id) {
        return getResource().getString(id);
    }

    /*---------方便获得values下面的string.xml里面的String--------*/
    public static String getString(int id, Object object) {
        return getResource().getString(id,object);
    }

    /*---------方便获得values下面的string.xml里面的String[]--------*/
    public static String[] getStrArr(int id) {
        return getResource().getStringArray(id);
    }

    public static int getColor(int color) {
        return getResource().getColor(color);
    }

    /*---------得到包名--------*/
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /*---------得到主线程的ID-----------*/
    public static int getMainThreadID() {
        return MyApplication.getMainThreadId();
    }

    /*---------得到主线程的Handler-----------*/
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    /**
     * 把数据放到主线程中去操作
     *
     * @param task 传递过来的正常运行的线程
     */
    public static void postTaskSafe(Runnable task) {
        //获取到子线程ID
        long myTid = Process.myTid();
        //获取到主线程ID
        long mainThreadID = getMainThreadID();
        //判断当前的线程,不在主线程的话就把它放到主线程中去操作数据
        if (myTid == mainThreadID) {
            //当前线程等于主线程,直接操作数据
            task.run();
        } else {
            //当前线程不在主线程,就把数据放到主线程中去操作
            Handler handler = getHandler();
            handler.post(task);
        }
    }

    //dip转换px的公式方法
    public static int dip2px(int dip) {
        // denstity*dip=px;
        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    //px转换dip的公式方法
    public static int px2dip(int px) {
        // denstity*dip=px;
        float density = getResource().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }
}
