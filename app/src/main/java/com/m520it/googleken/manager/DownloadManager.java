package com.m520it.googleken.manager;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.factory.ThreadPoolFactory;
import com.m520it.googleken.utils.CommonUtils;
import com.m520it.googleken.utils.FileUtils;
import com.m520it.googleken.utils.HttpUtils;
import com.m520it.googleken.utils.IOUtils;
import com.m520it.googleken.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lihoujing2ken
 * @time 2017/1/17  20:13
 * @desc 下载的各种状态, 还要操作的方法都在这操作
 */
public class DownloadManager {
    private static DownloadManager instance;//做个单例
    /**
     * ###########下载的所有状态###########
     **/
    public static final int STATE_UNDOWNLOAD = 0;//未下载
    public static final int STATE_DOWNLOADING = 1;//下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;//暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;//等待下载
    public static final int STATE_DOWNLOADFAILED = 4;//下载失败
    public static final int STATE_DOWNLOADED = 5;//下载完成
    public static final int STATE_INSTALLED = 6;//安装
    /**
     * ######################
     **/
    //key 包名 value 每一个被点击app下载的DownloadInfo信息
    public Map<String, DownloadInfo> hashMaps = new HashMap<>();
    private DownloadTask mTask;

    public DownloadManager() {
    }

    //单例的方法
    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    //获取下载数据,下载的具体操作方式
    public void download(DownloadInfo downloadInfo) {
        hashMaps.put(downloadInfo.packageName, downloadInfo);
        /**#############当前状态:未下载的状态###########**/
        downloadInfo.mCurrentState = STATE_UNDOWNLOAD;
        notifyObservers(downloadInfo);
        /**########################**/

        /**#############当前状态: 等待下载状态###########**/
        downloadInfo.mCurrentState = STATE_WAITINGDOWNLOAD;
        notifyObservers(downloadInfo);
        /**########################**/

        //得到线程池开始下载
        mTask = new DownloadTask(downloadInfo);
        downloadInfo.mCurrentTask = mTask;
        //这是线程池封装的方法
        ThreadPoolFactory.createDownLoadThreadPool().execute(mTask);
    }
    public DownloadInfo getDownloadInfo(ItemInfoBean mItemInfoBean) {
        DownloadInfo downloadInfo=new DownloadInfo();

        /** 其实就是给downloadInfo 里面的一些字段赋值
         *  赋值分为两种: 一种是普通赋值. 一种是关键赋值
         *
         */
        /**##########文件的普通信息############**/
        //文件的长度
        downloadInfo.maxSize=mItemInfoBean.size;
        //文件下载后的唯一名称.apk
        String saveName=mItemInfoBean.packageName+".apk";
        //文件存放的位置
        String dir = FileUtils.getDir("apk");
        File saveFile=new File(dir,saveName);
        //文件的绝对路径
        downloadInfo.savePath=saveFile.getAbsolutePath();
        //文件的下载链接地址
        downloadInfo.name=mItemInfoBean.downloadUrl;
        //文件的包名
        downloadInfo.packageName=mItemInfoBean.packageName;
        /**######################**/
        /**###########文件的关键赋值###########**/
        //判断当前的apk是否已经下载
        boolean installed = CommonUtils.isInstalled(UIUtils.getContext(), downloadInfo.packageName);
        if (installed){
            downloadInfo.mCurrentState=STATE_INSTALLED;
            return downloadInfo;
        }
        //判断是否下载完成
        File file=new File(downloadInfo.savePath);
        if (file.length()==downloadInfo.maxSize){
            //下载完成
            downloadInfo.mCurrentState=STATE_DOWNLOADED;
            notifyObservers(downloadInfo);
            return downloadInfo;
        }
        //点击下载后的按钮得到一个下载中的状态
        if (hashMaps.containsKey(downloadInfo.packageName)){
            notifyObservers(downloadInfo);
            return hashMaps.get(downloadInfo.packageName);
        }
        //走到这里说明没被下载
        downloadInfo.mCurrentState=STATE_UNDOWNLOAD;
        notifyObservers(downloadInfo);
        return downloadInfo;
    }
    //暂停
    public void AppPauseDownload(DownloadInfo downloadInfo) {
        // 1 改变当前的状态
        downloadInfo.mCurrentState=STATE_PAUSEDOWNLOAD;
        // 2 发布消息
        notifyObservers(downloadInfo);
    }
    //取消下载执行的方法
    public void AppCancelDownload(DownloadInfo downloadInfo) {
        //从线程池中删掉当前的线程
        ThreadPoolFactory.createDownLoadThreadPool().remove(downloadInfo.mCurrentTask);
        //修改当前的状态
        downloadInfo.mCurrentState=STATE_UNDOWNLOAD;
        // 发消息改变UI
        notifyObservers(downloadInfo);
    }
    //数据的加载操作
    private class DownloadTask implements Runnable {
        private DownloadInfo downloadInfo;//用于接收数据
        public DownloadTask(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }
        @Override
        public void run() {
            InputStream in = null;//声明流
            FileOutputStream fileOutputStream = null;//声明流
            long initRante = 0;//用于断点下载
            //在下载前,先判断是否已经下载了该文件的一部分
            String savePath = downloadInfo.savePath;
            File saveFile = new File(savePath);
            if (saveFile.exists()) {
                //说明以前下载过
                initRante = saveFile.length();
            }
            // 下载的进度条继续在原来的基础上面添加
            downloadInfo.mCurrentProcess = initRante;
            try {
                /**#############当前状态: 正在下载状态###########**/
                downloadInfo.mCurrentState = STATE_DOWNLOADING;
                //发布消息
                notifyObservers(downloadInfo);
                /**########################**/
                //真正下载的逻辑此处开始
                OkHttpClient okHttpClient = new OkHttpClient();
                String url = Constants.URLS.DOWNLOADAPP;
                HashMap<String, Object> paramsHashMap = new HashMap<>();
                paramsHashMap.put("name", downloadInfo.name);
                paramsHashMap.put("range", String.valueOf(initRante));
                String urlParamsByMap = HttpUtils.getUrlParamsByMap(paramsHashMap);
                url = url + urlParamsByMap;
                Request request = new Request.Builder().get().url(url).build();
                Response response = okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    /**#############当前状态: 正在下载状态###########**/
                    downloadInfo.mCurrentState = STATE_DOWNLOADING;
                    //发布消息
                    notifyObservers(downloadInfo);
                    /**########################**/
                    in = response.body().byteStream();
                    //得到流转换为一个文件中的.apk
                    File saveApp = new File(downloadInfo.savePath);
                    // 以前已经下载的一部分内容,应该继续在上面添加
                    fileOutputStream = new FileOutputStream(saveApp, true);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    boolean isPause = false;
                    while ((len = in.read(buffer)) != -1) {
                        // 先判断是否是暂停的状态
                        if (downloadInfo.mCurrentState == STATE_PAUSEDOWNLOAD) {
                            //代表的是暂停下载
                            isPause = true;
                            break;
                        }

                        /**#############当前状态: 正在下载状态###########**/
                        //将变化的长度一直加到进度条中
                        downloadInfo.mCurrentProcess += len;
                        downloadInfo.mCurrentState = STATE_DOWNLOADING;
                        //发布消息
                        notifyObservers(downloadInfo);
                        /**########################**/
                    /**###########下面是下载的操作###########**/
                        fileOutputStream.write(buffer, 0, len);
                        if (saveApp.length() == downloadInfo.maxSize) {
                            break;
                        }
                    }

                    if (isPause) {
                        //说明是一个暂停下载
                    } else {
                        //代码走到这里,说明app下载完成
                        /**#############当前状态: 完成下载状态###########**/
                        downloadInfo.mCurrentState = STATE_DOWNLOADED;
                        //发布消息
                        notifyObservers(downloadInfo);
                        /**########################**/
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                //代码走到这里,说明app下载完成
                /**#############当前状态: 失败下载状态###########**/
                downloadInfo.mCurrentState = STATE_DOWNLOADFAILED;
                //发布消息
                notifyObservers(downloadInfo);
                /**########################**/
            } finally {
                //关闭流
                IOUtils.close(in);
                IOUtils.close(fileOutputStream);
            }
        }
    }


    /**
     * ###########自定义的观察者和被观察者###########
     **/
    // 1 定义 接口对象
    public interface MessageObserver {
        void messageChanger(DownloadInfo downloadInfo);
    }

    // 2定义一个接口的集合
    public List<MessageObserver> lists = new ArrayList<MessageObserver>();

    //3 添加观察者的方法
    public void addObserver(MessageObserver observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        synchronized (this) {
            if (!lists.contains(observer))
                lists.add(observer);
        }
    }

    //4移除观察者的方法
    public synchronized void deleteObserver(MessageObserver observer) {
        lists.remove(observer);
    }

    public void notifyObservers(DownloadInfo downloadInfo) {
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).messageChanger(downloadInfo);
        }

    }
    /**######################**/
}
