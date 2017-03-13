package com.m520it.googleken.factory;

import com.m520it.googleken.manager.ThreadPoolProxy;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  18:47
 * @desc (单例懒汉式) 专门用于给线程池生产线程的
 * 一般会对应两种线程:下载资源/加载图片
 * 1. 用于apk的下载
 * 2. 用于普通的下载:不如图片
 */
public class ThreadPoolFactory {
    private static ThreadPoolProxy mDownLoadThreadPool;//专门用来下载的线程池
    private static ThreadPoolProxy mDefaultThreadPool;//专门用来获取普通数据线程池

    //专门用来下载的线程池
    public static ThreadPoolProxy createDownLoadThreadPool() {
        if (mDownLoadThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                //初始化线程池的对线
                mDownLoadThreadPool = new ThreadPoolProxy(5, 5);
            }
        }
        return mDownLoadThreadPool;
    }
    //专门用来获取普通数据线程池
    public static ThreadPoolProxy createDefaultThreadPool() {
        if (mDefaultThreadPool == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDefaultThreadPool == null) {
                    //初始化线程池
                    mDefaultThreadPool = new ThreadPoolProxy(5, 5);
                }
            }
        }
        return mDefaultThreadPool;
    }
}
