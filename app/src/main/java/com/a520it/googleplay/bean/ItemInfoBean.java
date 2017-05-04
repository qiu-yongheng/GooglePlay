package com.a520it.googleplay.bean;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/22  14:04
 * @desc ${TODD}
 */
public class ItemInfoBean {
    /**
     * id : 1525489
     * des : 产品介绍：google市场app测试。
     * packageName : com.m520it.www
     * stars : 5
     * iconUrl : app/com.m520it.www/icon.jpg
     * name : 小码哥程序员
     * downloadUrl : app/com.m520it.www/com.m520it.www.apk
     * size : 91767
     */
    public int id;//应用的id
    public String des;//应用的描述
    public String packageName;//应用的包名
    public float stars;//应用的评分
    public String iconUrl;//应用的图标地址
    public String name;//应用名
    public String downloadUrl;//应用的下载地址
    public long size;//应用的大小


    /**-------------详情里面额外添加的字段--------------**/
    public String author;//作者
    public String date;//时间
    public String downloadNum;//下载数
    public String version;//版本
    public List<ItemInfoSafeBean> safe;//
    public List<String> screen;//

    public class ItemInfoSafeBean {
        public String safeDes;//
        public int safeDesColor;//
        public String safeDesUrl;//
        public String safeUrl;//
    }
}
