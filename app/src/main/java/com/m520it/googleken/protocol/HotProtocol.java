package com.m520it.googleken.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m520it.googleken.base.BaseProtocol;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  9:39
 * @desc HotFragment 界面的数据协议
 */
public class HotProtocol extends BaseProtocol<List<String>> {
    @Override
    public List<String> getJsonParseBean(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string,new TypeToken<List<String>>(){}.getType());
    }

    @Override
    public String getInteger() {
        return "hot";
    }
}
