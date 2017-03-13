package com.m520it.googleken.protocol;

import com.m520it.googleken.Bean.CategoryInfoBean;
import com.m520it.googleken.base.BaseProtocol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  19:42
 * @desc 给CategoryFragment界面操作网络数据的协议
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {
    @Override
    public List<CategoryInfoBean> getJsonParseBean(String string) {
        //用于存放解释出来的数据
        List<CategoryInfoBean> categoryInfoBeenList = new ArrayList<>();
        /**
         * 这个Bean数据比较奇葩
         * 所以使用数组的形式来解释
         */
        try {
            //使用原生的JSONArray 来解释比较奇葩的Bean
            JSONArray jsonArray = new JSONArray(string);
            //数组就用for循环迭代
            for (int i = 0; i < jsonArray.length(); i++) {
                /*---------获取到第一个-----------*/
                //创建个 CategoryInfoBean 对象,用于装载解析好的数据
                CategoryInfoBean beanTitle = new CategoryInfoBean();
                //解释数组中的每一个 索引 得到一个对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //再以该对象获取之数据
                String title = jsonObject.getString("title");
                //获取到数据之后,就把该数据存放进 CategoryInfoBean 中对应的数据
                beanTitle.title = title;
                beanTitle.isTitle = true;//添加了数据就把状态码添加进去
                //把获取到的第一个添加到List<CategoryInfoBeen> 中去
                categoryInfoBeenList.add(beanTitle);


                /*---------获取第二个,第二个是数组,再慢慢添加-----------*/
                JSONArray jsonArrayInfo = jsonObject.getJSONArray("infos");
                for (int j = 0; j < jsonArrayInfo.length(); j++) {
                    CategoryInfoBean beanInfos = new CategoryInfoBean();
                    //获取里面的对象
                    JSONObject jsonObj = jsonArrayInfo.getJSONObject(j);
                    String name1 = jsonObj.getString("name1");
                    beanInfos.name1 = name1;
                    String name2 = jsonObj.getString("name2");
                    beanInfos.name2 = name2;
                    String name3 = jsonObj.getString("name3");
                    beanInfos.name3 = name3;
                    String url1 = jsonObj.getString("url1");
                    beanInfos.url1 = url1;
                    String url2 = jsonObj.getString("url2");
                    beanInfos.url2 = url2;
                    String url3 = jsonObj.getString("url3");
                    beanInfos.url3 = url3;
                    //集合添加的的第二,三,,,,,个CategoryInfobean
                    categoryInfoBeenList.add(beanInfos);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return categoryInfoBeenList;
    }

    @Override
    public String getInteger() {
        return "category";
    }
}
