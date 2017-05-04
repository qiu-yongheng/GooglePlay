package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.HomeInfoBean;
import com.google.gson.Gson;

/**
 * @author 邱永恒
 * @time 2016/8/21  10:19
 * @desc ${TODD}
 */
public class HomeProtocol extends BaseProtocol<HomeInfoBean>{




    /**
     * 解析网络请求回来的数据, 封装到一个对象中
     * @param
     * @param resultJson
     * @return
     */
    @Override
    public HomeInfoBean parseJsonString(String resultJson) {
        Gson gson = new Gson();

        return gson.fromJson(resultJson, HomeInfoBean.class);
    }

    /**
     * 返回协议的关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "home";
    }

}
