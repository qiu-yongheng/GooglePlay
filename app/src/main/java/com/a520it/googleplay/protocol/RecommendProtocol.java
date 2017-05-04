package com.a520it.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/24  10:00
 * @desc ${TODD}
 */
public class RecommendProtocol extends BaseProtocol<List<String>>{
    @Override
    public List<String> parseJsonString(String resultJson) {
        Gson gson = new Gson();
        return gson.fromJson(resultJson, new TypeToken<List<String>>(){}.getType());
    }

    @Override
    public String getInterfaceKey() {
        return "recommend";
    }
}
