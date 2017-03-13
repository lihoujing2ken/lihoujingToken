package com.m520it.googleken.protocol;

import com.google.gson.Gson;
import com.m520it.googleken.base.BaseProtocol;
import com.m520it.googleken.Bean.HomeInfoBean;

/**
 * @author lihoujing2ken
 * @time 2017/1/12  13:44
 * @desc HomeFragment界面的获取网络数据协议
 */
public class HomeProtocol extends BaseProtocol<HomeInfoBean> {

    /**
     * 不确定返回的是什么对象,所有这个方法就是抽象的
     *
     * @param string 这个是使用OKHttp请求回来的Json数据
     * @return 这里返回的是解析好的Bean的对象
     */
    @Override
    public HomeInfoBean getJsonParseBean(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, HomeInfoBean.class);
    }

    /**
     * 这是为HomeFragment获取数据的,所有返回的就是home
     *
     * @return 类型就是home
     */
    @Override
    public String getInteger() {
        return "home";
    }
}
