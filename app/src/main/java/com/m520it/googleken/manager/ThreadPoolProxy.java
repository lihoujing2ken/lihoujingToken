package com.m520it.googleken.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  18:28
 * @desc 线程的代理, 在这就可以把线程池中不需要的数据给写死, 还有就是初始化
 * 代理写完了,就创建一个工厂类,专门用于生产线程池
 */
public class ThreadPoolProxy {

    private int mCorePoolSize;//核心线程个数
    private int mMaximumPoolSize;//最大线程个数
    public ThreadPoolExecutor mThreadPoolExecutor;//线程池的执行者

    //将需要的参数使用构造器传递过来
    public ThreadPoolProxy(int maximumPoolSize, int corePoolSize) {
        mMaximumPoolSize = maximumPoolSize;
        mCorePoolSize = corePoolSize;
    }

    //提交任务
    public void submit(Runnable task) {
        initThreadPoolExecute();
        mThreadPoolExecutor.submit(task);
    }

    //执行任务
    public void execute(Runnable task) {
        initThreadPoolExecute();
        mThreadPoolExecutor.execute(task);
    }

    //删除任务
    public void remove(Runnable task) {
        initThreadPoolExecute();
        mThreadPoolExecutor.remove(task);
    }

    //初始化 ThreadPoolExecutor 线程池
    private void initThreadPoolExecute() {
        //初始化前要判断一下线程池是否为空,是否为关闭,是否创建
        if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
            //以上条件不成立的时候再开始创建线程池,创建线程池之前上个同步锁
            synchronized (ThreadPoolProxy.class) {
                if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
                    int corePoolSize = mCorePoolSize;//核心线程数
                    int maximumPoolSize = mMaximumPoolSize;//最大线程数
                    long keepAliveTime = 5000;//线程空闲后保存的时间长度
                    TimeUnit unit = TimeUnit.MILLISECONDS;//报时间长度的单位
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();//任务的队列
                    ThreadFactory threadFactory = Executors.defaultThreadFactory(); //线程工厂
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();//异常捕获器

                    //初始化线程池,需要的参数再外面传进来
                    mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                            unit, workQueue, threadFactory, handler);
                }
            }

        }
    }

}
