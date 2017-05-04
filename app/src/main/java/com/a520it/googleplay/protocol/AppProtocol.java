package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.ItemInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/22  14:06
 * @desc ${TODD}
 */
public class AppProtocol extends BaseProtocol<List<ItemInfoBean>>{



    /**
     * 解析json数据, 封装到指定对象中
     * @param resultJson
     * @return
     */
    @Override
    public List<ItemInfoBean> parseJsonString(String resultJson) {
        Gson gson = new Gson();

        //jsonString-->List/map-->泛型解析(解析的数据封装到包含ItemInfoBean的集合中)
        return gson.fromJson(resultJson, new TypeToken<List<ItemInfoBean>>() {

        }.getType());

    }

    /**
     * 返回协议的关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "app";
    }
}
