package com.a520it.googleplay.manager;

import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.factory.ThreadPoolProxyFactory;
import com.a520it.googleplay.utils.CommonUtils;
import com.a520it.googleplay.utils.FileUtils;
import com.a520it.googleplay.utils.HttpUtils;
import com.a520it.googleplay.utils.IOUtils;
import com.a520it.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 邱永恒
 * @time 2016/8/25  19:41
 * @desc 下载管理器, 负责下载模块, 封装和下载相关的逻辑
 * 做成单例模式
 */
public class DownLoadManager {
    private static DownLoadManager instance;


    public static final int STATE_UNDOWNLOAD = 0;//未下载
    public static final int STATE_DOWNLOADING = 1;//下载中
    public static final int STATE_PAUSEDOWNLOAD = 2;//暂停下载
    public static final int STATE_WAITINGDOWNLOAD = 3;//等待下载
    public static final int STATE_DOWNLOADFALSED = 4;//下载失败
    public static final int STATE_DOWNLOADED = 5;//下载完成
    public static final int STATE_INSTALLED = 6;//已安装

    /**
     * 保存App下载状态的集合
     * 参数一: 包名
     * 参数二: 封装了下载信息的类
     */
    private Map<String, DownLoadInfo> cacheDownLoadInfoMap = new HashMap<>();


    //私有化构造器
    private DownLoadManager() {
    }

    //提供一个获取对象的方法(懒汉单例模式)
    public static DownLoadManager getInstance() {
        if (instance == null) {
            //加锁
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    //创建对象
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * 点击后, 开始下载
     */
    public void downLoad(DownLoadInfo downLoadInfo) {//下载的信息封装到对象中, 传入
        /**
         * 当点击下载按钮, 就保存当前下载的包名, 和DownLoadInfo对象
         */
        cacheDownLoadInfoMap.put(downLoadInfo.packageName, downLoadInfo);


        /**###################当前状态:未下载##################**/
        downLoadInfo.curState = STATE_UNDOWNLOAD;

        //通知观察者刷新UI
        notifyObservers(downLoadInfo);
        /**#####################################**/

        /**##################当前状态:等待下载###################**/
        downLoadInfo.curState = STATE_WAITINGDOWNLOAD;

        //通知观察者刷新UI
        notifyObservers(downLoadInfo);
        /**#####################################**/

        /**
         * 预先把状态设置为等待状态
         *
         */



        //异步下载
        DownLoadTase tase = new DownLoadTase(downLoadInfo);

        //记录下当前任务的下载任务
        downLoadInfo.downLoadTask = tase;

        //创建一个下载的线程
        ThreadPoolProxyFactory.createDownLoadThreadPoolProxy().execute(tase);
    }

   


    class DownLoadTase implements Runnable {
        private final DownLoadInfo downLoadInfo;

        //初始化下载的连接
        public DownLoadTase(DownLoadInfo downLoadInfo) {
            this.downLoadInfo = downLoadInfo;
        }

        @Override
        public void run() {
            /**###################当前状态:下载中##################**/
            downLoadInfo.curState = STATE_DOWNLOADING;

            //通知观察者刷新UI
            notifyObservers(downLoadInfo);
            /**#####################################**/
            /**------------读取断点下载信息------------**/
            //设置默认下载进度为0
            long initRange = 0;

            //获取文件
            File saveApk = new File(downLoadInfo.savePath);

            if (saveApk.exists()) {//如果文件存在
                //获取下载的进度
                initRange = saveApk.length();
            }

            //处理上一次的ui进度
            downLoadInfo.progress = initRange;







            InputStream in = null;
            FileOutputStream out = null;

            //创建okhttpclient对象
            OkHttpClient okHttpClient = new OkHttpClient();

            String url = Constants.URLS.DOWNLOADURL;

            //拼装下载链接
            HashMap<String, Object> params = new HashMap<>();
            params.put("name", downLoadInfo.name);
            params.put("range", initRange + "");//从哪里开始下载


            //http://localhost:8080/GooglePlayServer/download?name=&range=0
            //解析map, 拼装字符串
            url = url + HttpUtils.getUrlParamsByMap(params);

            //2. 创建请求
            Request request = new Request.Builder().get().url(url).build();

            try {
                //3. 发送请求
                Response response = okHttpClient.newCall(request).execute();

                if (response.isSuccessful()) {
                    //请求成功
                    //转换成字符流, 写入到文件中
                    in = response.body().byteStream();


                    //写文件的时候, 以追加的方式写入(true)
                    out = new FileOutputStream(saveApk, true);

                    byte[] buffer = new byte[1024];

                    int len = -1;


                    boolean isPause = false;
                    while ((len = in.read(buffer)) != -1) {
                        //判断是否暂停
                        if (downLoadInfo.curState == STATE_PAUSEDOWNLOAD) {
                            isPause = true;

                            //跳出循环
                            break;
                        }



                        /**###################当前状态:下载中##################**/
                        downLoadInfo.curState = STATE_DOWNLOADING;

                        //通知观察者刷新UI
                        notifyObservers(downLoadInfo);
                        /**#####################################**/

                        out.write(buffer, 0, len);

                        //记录当前下载进度
                        downLoadInfo.progress += len;

                        if (saveApk.length() == downLoadInfo.max) {
                            break;
                        }
                    }


                    //如果暂停了, 直接跳过
                    if (isPause) {

                    } else {
                        /**###################当前状态:下载完成##################**/
                        downLoadInfo.curState = STATE_DOWNLOADED;

                        //通知观察者刷新UI
                        notifyObservers(downLoadInfo);
                        /**#####################################**/
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
                /**##################当前状态:下载失败###################**/
                downLoadInfo.curState = STATE_DOWNLOADFALSED;

                //通知观察者刷新UI
                notifyObservers(downLoadInfo);
                /**#####################################**/
            } finally {
                IOUtils.close(in);
                IOUtils.close(out);
            }

        }
    }


    /**
     * 暂停下载
     * @param downLoadInfo
     */
    public void pauseDownLoad(DownLoadInfo downLoadInfo) {
        //改变状态
        /**############暂停下载############**/
        downLoadInfo.curState = STATE_PAUSEDOWNLOAD;

        //发送消息, 更新UI
        notifyObservers(downLoadInfo);
        /**########################**/
    }

    /**
     * 取消下载
     * @param downLoadInfo
     */
    public void cancelDownLoad(DownLoadInfo downLoadInfo) {
        /**############取消下载############**/
        downLoadInfo.curState = STATE_UNDOWNLOAD;

        //发送消息, 更新UI
        notifyObservers(downLoadInfo);
        /**########################**/

        //从线程池中移除任务
        ThreadPoolProxyFactory.createDownLoadThreadPoolProxy().remove(downLoadInfo.downLoadTask);
    }










    /**
     * 根据传递过来的itemInfoBean信息, 返回一个DownLoadInfo对象
     *
     * @param itemInfoBean
     */
    public DownLoadInfo getDownLoadInfo(ItemInfoBean itemInfoBean) {
        //创建一个存放下载信息的类(下载路径, 保存地址...)
        DownLoadInfo info = new DownLoadInfo();

        String dir = FileUtils.getDir("apk");
        //设置下载文件的文件名
        String fileName = itemInfoBean.packageName + ".apk";
        //创建文件的保存路径
        File saveFile = new File(dir, fileName);


        //设置文件下载的路径
        info.name = itemInfoBean.downloadUrl;
        //获取下载文件的大小
        info.max = itemInfoBean.size;
        //获取下载文件的保存路径
        info.savePath = saveFile.getAbsolutePath();
        //获取下载文件的包名
        info.packageName = itemInfoBean.packageName;


        /**
         * 封装程序的下载状态
         */
        //判断包是否安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), info.packageName)) {
            /**-----已安装-----**/
            info.curState = STATE_INSTALLED;
            return info;
        }


        //获取保存路径
        File saveApk = new File(info.savePath);
        if (saveApk.exists() && saveApk.length() == info.max) {
            //下载完成
            info.curState = STATE_DOWNLOADED;

        }


        /**
         * 下载中
         * 暂停下载
         * 等待下载
         * 下载失败
         */
        //判断程序是否触发了下载
        if (cacheDownLoadInfoMap.containsKey(itemInfoBean.packageName)) {
            info = cacheDownLoadInfoMap.get(itemInfoBean.packageName);
            return info;
        }




        //未下载(默认值)
        info.curState = STATE_UNDOWNLOAD;
        return info;
    }



    /**---------自己实现观察者设计模式, 当状态改变时, 发送消息(downLoadInfo)给观察者(Holder)----------**/
    // 1. 定义接口和接口方法
    public interface DownLoadInfoObserver {
        void onDownLoadInfoChanged(DownLoadInfo downLoadInfo);
    }

    // 2. 定义接口对象的集合, 也就是所有的观察者
    private List<DownLoadInfoObserver> observer = new ArrayList<>();

    // 3. 添加观察者
    public synchronized void addObserver(DownLoadInfoObserver o) {
        //判断观察者是否为空
        if (o == null) {
            throw new NullPointerException();
        }
        //判断集合中是否存在
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }

    // 4. 移除观察者
    public synchronized void removeObserver(DownLoadInfoObserver o) {
        //
        if (o != null && observer.contains(o)) {
            observer.remove(o);
        }
    }

    // 5. 通知所有的观察者-->发布消息
    public void notifyObservers(DownLoadInfo downLoadInfo) {
        // 获取每一个观察者
        for (DownLoadInfoObserver o : observer) {
            //让观察者去调用接口的方法
            o.onDownLoadInfoChanged(downLoadInfo);
        }
    }
}
