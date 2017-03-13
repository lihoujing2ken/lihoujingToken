package com.m520it.googleken.manager;
/**
 * @author lihoujing2ken
 * @time 2017/1/17  20:12
 * @desc 用于 DownloadManager 存放各种需要的数据,相当于一个包囊
 *
 */
public class DownloadInfo {
    public String name;//app下载的路径
    public String  savePath;//下载的app存放文件的路径
    public long maxSize;//当前apk大小
    public int mCurrentState= DownloadManager.STATE_UNDOWNLOAD;//默认都是从这个未下载开始的

    public String packageName;//包名
    public long mCurrentProcess;//当前app下载的进度
    public Runnable mCurrentTask;//当前正在执行的任务
}
