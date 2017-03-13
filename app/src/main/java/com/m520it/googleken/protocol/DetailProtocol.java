package com.m520it.googleken.protocol;

import com.google.gson.Gson;
import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.base.BaseProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  14:28
 * @desc 为DetailActivity界面获取网络数据协议
 */
public class DetailProtocol extends BaseProtocol<ItemInfoBean> {
    private String mPackageName;//用于接收包名的数据

    public DetailProtocol(String packageName) {
        this.mPackageName = packageName;
    }

    /**
     * 碰到一个难题了,这个的网络请求数据是不一样的了,怎么解决呢?
     * 这就要在封装的网络请求方法中抽取方法来实现
     * @param string 网络请求获取到的数据
     * @return
     */
    @Override
    public ItemInfoBean getJsonParseBean(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, ItemInfoBean.class);
    }

    @Override
    public String getInteger() {
        return "detail";
    }

    //复写父类方法,用于换取访问的路径
    @Override
    public Map<String, Object> getHashMap(int index) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("packageName", mPackageName);
        return hashMap;
    }
}
