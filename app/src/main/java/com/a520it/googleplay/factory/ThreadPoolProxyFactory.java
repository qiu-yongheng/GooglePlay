package com.a520it.googleplay.factory;

import com.a520it.googleplay.manager.ThreadPoolProxy;

/**
 * @author 邱永恒
 * @time 2016/8/20  11:51
 * @desc 线程池代理的工厂类
 */
public class ThreadPoolProxyFactory {
    //普通的线程池工厂
    static ThreadPoolProxy mNormalThreadPoolProxy;
    //下载的线程池工厂
    static ThreadPoolProxy mDownLoadThreadPoolProxy;


    /**
     * 在线程中只创建一个普通线程池的代理
     * @return
     */
    public static ThreadPoolProxy createNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            //加锁
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }


    /**
     * 在线程中只创建一个下载线程池的代理
     * @return
     */
    public static ThreadPoolProxy createDownLoadThreadPoolProxy() {
        if (mDownLoadThreadPoolProxy == null) {
            //加锁
            synchronized (ThreadPoolProxyFactory.class) {
                if (mDownLoadThreadPoolProxy == null) {
                    mDownLoadThreadPoolProxy = new ThreadPoolProxy(5,5,3000);
                }
            }
        }
        return mDownLoadThreadPoolProxy;
    }
}
