package com.a520it.googleplay.bean;

import com.a520it.googleplay.manager.DownLoadManager;

/**
 * @author 邱永恒
 * @time 2016/8/25  19:56
 * @desc 封装下载信息的类
 */
public class DownLoadInfo {
    /**
     * 保存的路径
     */
    public static String savePath;
    /**
     * 下载的具体路径
     */
    public String name;

    /**
     * 文件的最大长度
     */
    public long max;

    /**
     * 设置默认的下载状态(默认未下载)
     */
    public int curState = DownLoadManager.STATE_UNDOWNLOAD;

    /**
     * 应用程序的包名
     */
    public String packageName;

    /**
     * 记录当前进度
     */
    public long progress;

    /**
     * 下载的任务
     */
    public Runnable downLoadTask;
}
