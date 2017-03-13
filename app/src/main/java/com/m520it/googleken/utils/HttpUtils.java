package com.m520it.googleken.utils;

import java.util.Map;

/**
 * @author lihoujing2ken
 * @time 2017/1/12  10:56
 * @desc 网络请求的拼接
 */
public class HttpUtils {
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }
}
