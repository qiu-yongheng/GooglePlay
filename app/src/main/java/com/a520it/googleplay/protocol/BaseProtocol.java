package com.a520it.googleplay.protocol;

import com.a520it.googleplay.base.MyApplication;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.FileUtils;
import com.a520it.googleplay.utils.HttpUtils;
import com.a520it.googleplay.utils.IOUtils;
import com.a520it.googleplay.utils.LogUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 邱永恒
 * @time 2016/8/21  10:29
 * @desc 网络请求协议的基类
 */
public abstract class BaseProtocol<T> {

    public String mResultJson;
    private T t;

    //    public BaseProtocol(int index) {
    //        loadData(index);
    //    }

    /**
     * 分页请求的索引
     *
     * @param
     * @return
     */
    public T loadData(int index) throws IOException {
        /**-----------从内存中读取数据----------**/
        //获取全局类
        MyApplication app = (MyApplication) UIUtils.getContext();
        Map<String, String> protocolCacheMap = app.getProtocolCacheMap();

        //得到缓存的关键字.index
        String key = generateKey(index);

        //判断内存中是否包含索引, 包含, 说明已经存到内存, 然后比较是否过期
        if (protocolCacheMap.containsKey(key)) {
            LogUtils.v("BaseProtocol", "从内存里加载数据--->" + key);
            //根据key获取value
            String memJsonString = protocolCacheMap.get(key);

            //解析json数据, 将数据封装到对象中
            t = parseJsonString(memJsonString);

            return t;
        }

        /**-------------从本地获取数据------------**/
        t = loadDataFromLocal(key);
        if (t != null) {
            LogUtils.v("BaseProtocol", "从本地加载数据--->" + getCacheFile(key).getAbsolutePath());
            return t;
        }


        /**-------------在网络请求中读取数据------------**/
        T t = loadDateFromNet(index);
        if (t != null) {
            return t;
        }

        return null;
    }

    /**
     * 从本地获取数据的方法
     *
     * @param key
     * @return
     */
    private T loadDataFromLocal(String key) {
        //1. 获取路径
        String dir = com.a520it.googleplay.utils.FileUtils.getDir("json");

        //2. 创建文件(文件路径, 文件名)
        File cacheFile = new File(dir, key);

        if (cacheFile.exists()) {//存在, 有缓存
            //读取第一行, 判断是否过期
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));

                //读取第一行
                String insertTimeStr = reader.readLine();

                //把String类型的数据解析成long类型的, 获取添加数据的时间
                long insertTime = Long.parseLong(insertTimeStr);

                //判断是否过期
                if (System.currentTimeMillis() - insertTime < Constants.PROTOCOLTIME) {
                    //读取缓存内容
                    String cacheJsonString = reader.readLine();

                    /**内存中没有, 在内存中保存一份**/
                    MyApplication app = (MyApplication) UIUtils.getContext();
                    app.getProtocolCacheMap().put(key, cacheJsonString);


                    //解析返回
                    return parseJsonString(cacheJsonString);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //关闭资源
                IOUtils.close(reader);
            }
        }
        return null;
    }


    /**
     * 返回网络协议的协议关键字.index
     *
     * @param index
     * @return
     */
    private String generateKey(int index) {
        Map<String, Object> parmasHashMap = getParmasHashMap(index);

        for (Map.Entry<String, Object> info : parmasHashMap.entrySet()) {
            String Key = info.getKey();//"index", "packageName"
            Object value = info.getValue();//0, 20, 40, 包名1, 包名2, 包名3

            return getInterfaceKey() + "." + value;
        }
        return "";
    }


    /**
     * 在网络请求中读取数据
     *
     * @param index
     * @return
     */
    private T loadDateFromNet(int index) throws IOException{
        //创建OKHttpClient实例对象
        OkHttpClient okHttpClient = new OkHttpClient();


        //获取谷歌市场的url地址
        String url = Constants.URLS.BASEURL + getInterfaceKey() + "?";


        //参数
        //暴露一个方法, 方便修改参数
        Map<String, Object> parmasMap = getParmasHashMap(index);
        String urlParamsByMap = HttpUtils.getUrlParamsByMap(parmasMap);


        //拼接后的结果
        url = url + urlParamsByMap;


        //创建一个请求对象
        Request request = new Request.Builder().get().url(url).build();


        //发送请求, 返回响应

            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {//响应成功
                String resultJson = response.body().string();
                //保存数据成成员变量
                mResultJson = resultJson;

                /**将数据保存到内存中**/
                LogUtils.v("BaseProtocol", "写缓存到内存中");
                MyApplication app = (MyApplication) UIUtils.getContext();
                String key = generateKey(index);
                app.getProtocolCacheMap().put(key, mResultJson);


                /**保存数据到本地**/
                LogUtils.v("BaseProtocol", "写缓存到本地中");
                BufferedWriter writer = null;
                try {
                    File cacheFile = getCacheFile(key);
                    writer = null;

                    writer = new BufferedWriter(new FileWriter(cacheFile));

                    //写入第一行---->插入时间
                    writer.write(System.currentTimeMillis() + "");
                    //换行
                    writer.newLine();


                    //清空换行字符
                    if (resultJson.contains("\r\n")) {
                        resultJson = resultJson.replace("\r\n", "");
                    }
                    if (resultJson.contains(" ")) {
                        resultJson = resultJson.replace(" ", "");
                    }





                    //写入第二行(写入json解析出来的String)
                    writer.write(resultJson);
                } finally {
                    //关闭资源
                    IOUtils.close(writer);

                }


                /**
                 * json解析交给子类去实现
                 */
                T t = parseJsonString(mResultJson);

                return t;
            }
        return null;
    }



    /**
     * 获取缓存文件夹
     *
     * @param key
     */
    private File getCacheFile(String key) {
        //1. 获取路径
        String dir = FileUtils.getDir("json");


        //2. 创建文件夹
        File cacheFile = new File(dir, key);


        LogUtils.v("BaseProtocol", cacheFile.toString());
        return cacheFile;
    }

    /**
     * 给子类去解析网络返回来的数据
     *
     * @param
     * @param resultJson
     * @return
     */
    public  T parseJsonString(String resultJson) {
        Gson gson = new Gson();

        Type actualType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Object o = gson.fromJson(resultJson, actualType);
        return (T) o;
    }


    /**
     * 请求参数的封装
     *
     * @return
     */
    public Map<String, Object> getParmasHashMap(int index) {
        Map<String, Object> defaulParamsHashMap = new HashMap<>();
        defaulParamsHashMap.put("index", String.valueOf(index));
        return defaulParamsHashMap;//默认传递index参数
    }

    /**
     * 只有调用者知道, 交给子类去实现
     *
     * @return 返回网络协议的协议关键字
     */
    public abstract String getInterfaceKey();
}
