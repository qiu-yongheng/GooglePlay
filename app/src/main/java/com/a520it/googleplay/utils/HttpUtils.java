package com.a520it.googleplay.utils;

import java.util.Map;

/**
 * @author 邱永恒
 * @time 2016/8/20  20:34
 * @desc 拼接网络路径
 */
public class HttpUtils {
    /**
     * 传递get参数对应的map集合, 返回拼接后的字符串信息
     * @param map
     * @return
     */
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
