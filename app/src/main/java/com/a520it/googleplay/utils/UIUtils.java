package com.a520it.googleplay.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.a520it.googleplay.base.MyApplication;

/**
 * @author 邱永恒
 * @time 2016/8/19  13:09
 * @desc ${TODD}
 */
public class UIUtils {
    //获取上下文
    public static Context getContext() {
        return MyApplication.getContext();
    }

    //获取resource对象
    public static Resources getResources() {
        return getContext().getResources();
    }

    //获取string.xml中的字符
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    //获取String.xml中的字符数组
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    //获取color.xml中的颜色值
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    //获取包名
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    //获取主线程的id
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    //获取主线程的Handler
    public static Handler getHandler() {
        return MyApplication.getHandler();
    }

    //安全的执行一个task
    public static void postTaskSafely(Runnable task) {
        //得到当前线程的id
        long curThreadId = android.os.Process.myTid();

        //获取主线程id
        long mainThreadId = getMainThreadId();

        if (curThreadId == mainThreadId) {
            //在主线程, 直接执行任务
            task.run();
        } else {
            //在子线程, 把任务交给主线程Handler执行
            //获取主线程Handler
            Handler handler = getHandler();
            handler.post(task);
        }
    }

    public static int dip2Px(int dip) {
        float density = getResources().getDisplayMetrics().density;

        int ppi = getResources().getDisplayMetrics().densityDpi;

        int px = (int)(dip * density + .5f);

        return px;
    }


    public static int px2Dip(int px) {
        float density = getResources().getDisplayMetrics().density;

        int dp = (int)(px / density + .5f);

        return dp;
    }


    /**
     * 得到String.xml中的字符, 带占位符
     * @param resId
     * @param formatArgs
     * @return
     */
    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }
}
