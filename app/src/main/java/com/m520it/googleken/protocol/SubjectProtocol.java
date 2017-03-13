package com.m520it.googleken.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.m520it.googleken.Bean.SubjectBean;
import com.m520it.googleken.base.BaseProtocol;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  19:02
 * @desc SubjectFragment 界面的网络协议
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {
    @Override
    public List<SubjectBean> getJsonParseBean(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<List<SubjectBean>>() {
        }.getType());
    }

    @Override
    public String getInteger() {
        return "subject";
    }
}
