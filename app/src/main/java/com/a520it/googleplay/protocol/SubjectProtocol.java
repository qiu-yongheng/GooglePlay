package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.SubjectInfoBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/23  18:58
 * @desc ${TODD}
 */
public class SubjectProtocol extends BaseProtocol<List<SubjectInfoBean>>{
    /**
     * 解析数据
     * @param resultJson
     * @return
     */
    @Override
    public List<SubjectInfoBean> parseJsonString(String resultJson) {
        Gson gson = new Gson();

        return gson.fromJson(resultJson, new TypeToken<List<SubjectInfoBean>>(){}.getType());
    }

    /**
     * 设置关键字
     * @return
     */
    @Override
    public String getInterfaceKey() {
        return "subject";
    }
}
