package com.m520it.googleken.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.base.BaseProtocol;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  11:15
 * @desc game界面获取网络数据的协议
 */
public class GameProtocol extends BaseProtocol<List<ItemInfoBean>> {
    @Override
    public List<ItemInfoBean> getJsonParseBean(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<List<ItemInfoBean>>() {
        }.getType());
    }

    @Override
    public String getInteger() {
        return "game";
    }
}
