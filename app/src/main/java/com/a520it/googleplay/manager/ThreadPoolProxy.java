package com.a520it.googleplay.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 邱永恒
 * @time 2016/8/20  9:00
 * @desc 线程池的代理
 */
public class ThreadPoolProxy {
    private final int mCorePoolSize;
    private final int mMaximunPoolSize;
    private final long mKeepAliveTime;


    //构造方法
    public ThreadPoolProxy(int corePoolSize,//核心池的大小
                           int maximunPoolSize,//线程池的最大线程数
                           long keepAliveTime) {//保持时间

        this.mCorePoolSize = corePoolSize;
        this.mMaximunPoolSize = maximunPoolSize;
        this.mKeepAliveTime = keepAliveTime;

        //初始化线程的方法
        initThreadPoolExecutor();
    };


    //创建一个线程池的代理
    ThreadPoolExecutor mExecutor;

    //提交任务
    public void submit(Runnable task) {
        mExecutor.submit(task);
    }

    //执行任务
    public void execute(Runnable task) {
        //获取线程池中的线程执行任务
        mExecutor.execute(task);
    }

    //移除任务
    public void remove(Runnable task) {
        mExecutor.remove(task);
    }

    /**
     * 初始化线程的方法
     */
    private void initThreadPoolExecutor() {
        //当线程不存在, 或线程关闭, 才可以创建新线程
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {

            //加同步锁(保证线程安全)
            synchronized (ThreadPoolProxy.class) {

                //设置核心池的大小
                int corePoolSize = mCorePoolSize;
                //设置最大线程数
                int maximunPoolSize = mMaximunPoolSize;
                //设置保持时间
                long keepAliveTime = mKeepAliveTime;

                TimeUnit unit = TimeUnit.MILLISECONDS;
                BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                ThreadFactory threadFactory = Executors.defaultThreadFactory();
                RejectedExecutionHandler handler= new ThreadPoolExecutor.DiscardPolicy();

                //创建一个线程池代理
                mExecutor = new ThreadPoolExecutor(
                        corePoolSize,//核心线程数
                        maximunPoolSize,//最大线程池数
                        keepAliveTime,//保持时间
                        unit,//保持时间的单位
                        workQueue,//任务队列
                        threadFactory,//线程工厂
                        handler//异常捕获器
                );
            }



        }
    }


}
