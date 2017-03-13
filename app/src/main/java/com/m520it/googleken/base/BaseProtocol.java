package com.m520it.googleken.base;

import android.os.SystemClock;

import com.m520it.googleken.application.MyApplication;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.FileUtils;
import com.m520it.googleken.utils.HttpUtils;
import com.m520it.googleken.utils.IOUtils;
import com.m520it.googleken.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lihoujing2ken
 * @time 2017/1/12  13:52
 * @desc 抽取出获取网络数据的基类
 * 抽基类,就是把不确定的因素抽出来做抽象方法
 * <p>
 * 做缓存处理,一个保存在本地,一个保存在SD卡,先本地,再到SD卡
 */
public abstract class BaseProtocol<T> {
    public T getHomeInfoBean(int indext) throws IOException {
        T t = null;
        //从内存中获取
        t = getCacherData(indext);
        if (t != null)
            return t;

        //SD卡中获取
        t = getSDData(indext);
        if (t != null) {
            return t;
        }
        //从网络中加载
        t = getDataFotmNet(indext);
        return t;

    }

    //以返回的界面名和传递过来的页数作为key
    private String getKeyParams(int index) {
        /**
         * 这个key值是有不同的值得了,所以这里要取出
         */
        Map<String, Object> hashMap = getHashMap(index);
        //找到Map里面的条目
        for (Map.Entry<String, Object> info : hashMap.entrySet()) {
            //获取到key 和 vulse
            String key = info.getKey();
            Object value = info.getValue();
            /**
             *因为index会不一样,后面的传递的包名,所以就把index获取出来,返回回去做缓存
             */
            return getInteger() + "." + value;
        }
        return null;
    }

    private T getDataFotmNet(int indext) throws IOException {
        //使用OKHttp的同步来加载网络数据
        //"http://192.168.33.122:8080/GooglePlayServer/home?index=0"
        /*---------使用OKHttp从网络中获取数据-----------*/
        OkHttpClient okHttpCent = new OkHttpClient();
        String url = Constants.URLS.BASEURL + getInteger() + "?";
        /**
         * 访问的值改变的,所以这里要抽取个方法
         */
        Map<String, Object> homeHashMap = getHashMap(indext);
        //homeHashMap.put("index", String.valueOf(indext));
        String paramsByMap = HttpUtils.getUrlParamsByMap(homeHashMap);
        url = url + paramsByMap;
        Request request = new Request.Builder().get().url(url).build();
        Response execute = okHttpCent.newCall(request).execute();
        if (execute.isSuccessful()) {
            T t;
            BufferedWriter bufferedWriter = null;
            try {
                String string = execute.body().string();
                /*---------以上代码就是获取到了数据-----------*/
                //返回的类型不确定
                t = getJsonParseBean(string);
                // 保存内存
                MyApplication app = (MyApplication) UIUtils.getContext();
                // 1 得到key
                String key = getKeyParams(indext);
                app.getParams().put(key, string);
                //保存SD卡
                String path = FileUtils.getDir("json");
                //得到key
                File cacherSd = new File(path, key);
                //写入
                bufferedWriter = new BufferedWriter(new FileWriter(cacherSd));
                //第一行写个时间
                bufferedWriter.write(SystemClock.currentThreadTimeMillis() + "");
                bufferedWriter.newLine();
                bufferedWriter.write(string);
            } finally {
                //关闭流
                IOUtils.close(bufferedWriter);
            }
            return t;
        } else {
            return null;
        }
    }

    //用于给子类返回不同的访问地址
    public Map<String, Object> getHashMap(int indext) {
        //子类复写了之后就可以返回不同的值
        Map<String, Object> homeHashMap = new HashMap<>();
        homeHashMap.put("index", String.valueOf(indext));
        return homeHashMap;
    }

    //保存在SD卡中
    private T getSDData(int index) {
        BufferedReader reader = null;
        try {
            //保存在Sd卡中文件当中
            String path = FileUtils.getDir("json");
            //得到key
            String key = getKeyParams(index);
            File cacherSd = new File(path, key);
            // 先判断这个文件是否有值
            if (cacherSd.exists()) {
                //存在就读取到这个SD卡的文件路径
                reader = new BufferedReader(new FileReader(cacherSd));
                //获取当前时间和存文件的时间
                String StrTimer = reader.readLine();
                long oldTimer = Long.parseLong(StrTimer);
                long DirTimer = SystemClock.currentThreadTimeMillis() - oldTimer;
                //得到时间差,来判断
                if (DirTimer < Constants.SAFETIMER) {
                    //说明没有过期
                    String JsonStr = reader.readLine();
                    T t = getJsonParseBean(JsonStr);
                    //保存一份到内存
                    MyApplication app = (MyApplication) UIUtils.getContext();
                    app.getParams().put(key, JsonStr);
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //关闭流
            IOUtils.close(reader);
        }
        return null;
    }

    //在内存中获取
    private T getCacherData(int index) {
        T t;
        MyApplication app = (MyApplication) UIUtils.getContext();
        HashMap<String, String> appParams = app.getParams();
        // 1 得到key
        String key = getKeyParams(index);
        // 2在这个hash中看是否能够找到这个key,如果能够找到,如果内存中有
        if (appParams.containsKey(key)) {
            String JsonStr = appParams.get(key);
            t = getJsonParseBean(JsonStr);
            return t;
        }
        return null;
    }

    public abstract T getJsonParseBean(String string);

    public abstract String getInteger();
}
