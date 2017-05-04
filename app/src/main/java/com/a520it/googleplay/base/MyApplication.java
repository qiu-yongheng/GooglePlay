package com.a520it.googleplay.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邱永恒
 * @time 2016/8/19  12:53
 * @desc 最先运行的类, 全局初始化方法和常量
 */
public class MyApplication extends Application{

    private static Context mContext;
    private static Handler mHandler;
    private static int mMainThreadId;

    //定义一个map集合, 用于存放信息
    private Map<String, String> mProtocolCacheMap = new HashMap<>();

    /**
     * 提供一个GET方法返回集合
     * @return
     */
    public Map<String, String> getProtocolCacheMap() {
        return mProtocolCacheMap;
    }

    /**
     * 应用第一个运行的方法
     */
    @Override
    public void onCreate() {
        super.onCreate();

        /**全局上下文**/
        mContext = getApplicationContext();
        /**主线程handler**/
        mHandler = new Handler();
        /**主线程的id**/
        mMainThreadId = Process.myTid();
    }

    /**提供获取全局上下文的方法**/
    public static Context getContext() {
        return mContext;
    }

    /**提供获取全局主线程handler的方法**/
    public static Handler getHandler() {
        return mHandler;
    }

    /**获取主线程的id**/
    public static long getMainThreadId() {
        return mMainThreadId;
    }
}
