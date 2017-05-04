package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邱永恒
 * @time 2016/8/24  14:49
 * @desc ${TODD}
 */
public class DetailProtocol extends BaseProtocol<ItemInfoBean>{

    private final String mPackageName;



    @Override
    public String getInterfaceKey() {
        return "detail";
    }

    /**
     *获取activity传过来的包名
     * 谁调用, 谁传过来
     * @param packageName
     */
    //
    public DetailProtocol(String packageName) {
        mPackageName = packageName;
    }



    @Override
    public Map<String, Object> getParmasHashMap(int index) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Constants.PACKAGENAME, mPackageName);
        return paramsMap;
    }

}
