package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.ItemInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/22  21:23
 * @desc 处理game页面的网络请求
 */
public class GameProtocol extends BaseProtocol<List<ItemInfoBean>>{
    /**
     * 将json解析成String
     * @param resultJson
     * @return
     */
    @Override
    public List<ItemInfoBean> parseJsonString(String resultJson) {
        Gson gson = new Gson();
        return gson.fromJson(resultJson, new TypeToken<List<ItemInfoBean>>(){

        }.getType());
    }

    /**
     * 设置标识码
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "game";
    }
}
