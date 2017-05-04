package com.a520it.googleplay.protocol;

import com.a520it.googleplay.bean.CategoryInfoBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/23  20:49
 * @desc ${TODD}
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>>{
    @Override
    public List<CategoryInfoBean> parseJsonString(String resultJson) {
        /**
         * 1. bean解析
         * 2. 泛型解析
         * 3. 结点解析
         */

        ArrayList<CategoryInfoBean> categoryInfoBeens = new ArrayList<>();

        try {
            //1. 获取最外层的根数据
            JSONArray rootJsonArr = new JSONArray(resultJson);

            for (int i = 0; i < rootJsonArr.length(); i++) {
                JSONObject jsonObject = rootJsonArr.getJSONObject(i);

                //获取标题
                String title = jsonObject.getString("title");

                CategoryInfoBean titleBean = new CategoryInfoBean();
                titleBean.title = title;
                titleBean.isTitle = true;

                //加入到集合中
                categoryInfoBeens.add(titleBean);

                JSONArray infosJsonArrs = jsonObject.getJSONArray("infos");

                for (int j = 0; j < infosJsonArrs.length(); j++) {
                    JSONObject infoJsonObj = infosJsonArrs.getJSONObject(j);

                    String name1 = infoJsonObj.getString("name1");
                    String name2 = infoJsonObj.getString("name2");
                    String name3 = infoJsonObj.getString("name3");
                    String url1 = infoJsonObj.getString("url1");
                    String url2 = infoJsonObj.getString("url2");
                    String url3 = infoJsonObj.getString("url3");

                    CategoryInfoBean infoBean = new CategoryInfoBean();
                    infoBean.name1 = name1;
                    infoBean.name2 = name2;
                    infoBean.name3 = name3;

                    infoBean.url1 = url1;
                    infoBean.url2 = url2;
                    infoBean.url3 = url3;

                    categoryInfoBeens.add(infoBean);
                }
            }
            return categoryInfoBeens;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getInterfaceKey() {
        return "category";
    }
}
