package com.m520it.googleken.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import java.util.HashMap;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:19
 * @desc 配置全局的信息
 * 温馨提示:配置文件中配置
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static int mMainThreadId;
    private static Handler mHandler;
    //创建一个全局的HashMap
    private HashMap<String, String> params = new HashMap<>();

    public HashMap<String, String> getParams() {
        return params;
    }

    /*---------开天方法-----------*/
    @Override
    public void onCreate() {
        /*---------配置全局的上下文-----------*/
        mContext = getApplicationContext();
        /*---------得到主线程的ID-----------*/
        mMainThreadId = Process.myTid();
        /*---------得到主线程 Handler-----------*/
        mHandler = new Handler();
        super.onCreate();
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getHandler() {
        return mHandler;
    }
}
