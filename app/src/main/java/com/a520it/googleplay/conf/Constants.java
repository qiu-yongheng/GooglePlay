package com.a520it.googleplay.conf;


import com.a520it.googleplay.utils.LogUtils;

/**
 * @author 王维波
 * @time 2016/8/19  10:44
 * @desc ${TODD}
 */
public class Constants {
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;//控制log输出级别在版本发布前修改值

    public static final int PROTOCOLTIME = 6 * 60 * 1000;

    public static final String PACKAGENAME = "packageName";


    public static final class URLS {
        public static final String BASEURL = "http://192.168.34.164:8080/GooglePlayServer/";
        public static final String IMGBASEURL = BASEURL + "image?name=";
        public static final String DOWNLOADURL = BASEURL + "download?";
    }
}
