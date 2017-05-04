package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.Button;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.utils.CommonUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.a520it.googleplay.views.ProgressButton;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 邱永恒
 * @time 2016/8/24  16:10
 * @desc ${TODD}
 */
public class DetailBottomHolder extends BaseHolder<ItemInfoBean> implements DownLoadManager.DownLoadInfoObserver {
    @Bind(R.id.app_detail_download_btn_favo)
    Button mAppDetailDownloadBtnFavo;
    @Bind(R.id.app_detail_download_btn_share)
    Button mAppDetailDownloadBtnShare;
    @Bind(R.id.app_detail_download_btn_download)
    ProgressButton mAppDetailDownloadBtnDownload;
    private ItemInfoBean mItemInfoBean;

    /**
     * @param data
     */
    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mItemInfoBean = data;

        /**-------2. 根据不同的状态给用户提示(修改下载按钮的UI)-------**/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mItemInfoBean);
        //根据返回的状态刷新UI
        refreshDownLoadUI(downLoadInfo);


    }

    private void refreshDownLoadUI(DownLoadInfo downLoadInfo) {
        //获取当前的下载状态
        int cuurState = downLoadInfo.curState;


        mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (cuurState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mAppDetailDownloadBtnDownload.setText("下载");
                break;

            case DownLoadManager.STATE_DOWNLOADING://下载中
                //设置下载的背景
                mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);


                //设置最大进度
                mAppDetailDownloadBtnDownload.setMax(downLoadInfo.max);

                //设置当前进度
                mAppDetailDownloadBtnDownload.setProgress(downLoadInfo.progress);

                //获取当前进度
                int progress = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);

                mAppDetailDownloadBtnDownload.setIsProgressEnable(true);

                mAppDetailDownloadBtnDownload.setText(progress + "%");
                break;

            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mAppDetailDownloadBtnDownload.setText("继续下载");
                break;

            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mAppDetailDownloadBtnDownload.setText("等待中...");
                break;

            case DownLoadManager.STATE_DOWNLOADFALSED://下载失败
                mAppDetailDownloadBtnDownload.setText("重试");
                break;

            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mAppDetailDownloadBtnDownload.setIsProgressEnable(false);

                mAppDetailDownloadBtnDownload.setText("安装");
                break;

            case DownLoadManager.STATE_INSTALLED://已安装
                mAppDetailDownloadBtnDownload.setText("打开");
                break;


        }
    }


    /**
     * 初始化控件
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_deital_bottom, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.app_detail_download_btn_download)
    public void clickBtnDownLoad(View view) {
        /**-------3. 根据不同的状态, 点击后触发不同的操作-------**/

        DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mItemInfoBean);
        int cuurState = info.curState;

        switch (cuurState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                //去下载
                doDownLoad(info);
                break;

            case DownLoadManager.STATE_DOWNLOADING://下载中
                //暂停下载
                pauseDownLoad(info);
                break;

            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                //断点继续下载
                doDownLoad(info);
                break;

            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                //取消下载
                cancelDownLoad(info);
                break;

            case DownLoadManager.STATE_DOWNLOADFALSED://下载失败
                //重试下载
                doDownLoad(info);
                break;

            case DownLoadManager.STATE_DOWNLOADED://下载完成
                //安装应用
                installApk(info);
                break;

            case DownLoadManager.STATE_INSTALLED://已安装
                //打开应用
                openApk(info);
                break;
        }


    }

    /**
     * 打开应用
     *
     * @param downLoadInfo
     */
    private void openApk(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 安装应用
     *
     * @param downLoadInfo
     */
    private void installApk(DownLoadInfo downLoadInfo) {
        File apkFile = new File(downLoadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }


    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void cancelDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().cancelDownLoad(downLoadInfo);
    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void pauseDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().pauseDownLoad(downLoadInfo);
    }

    /**
     * 开始下载, 继续下载, 重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().downLoad(downLoadInfo);
    }


    /**
     * 实现接口的方法
     * 被观察者发送消息时执行(状态改变时)
     *
     * @param downLoadInfo
     */
    @Override
    public void onDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        //过滤观察者
        if (!downLoadInfo.packageName.equals(mItemInfoBean.packageName)) {
            //如果返回的应用的包名和当前应用的包名不同, 直接返回
            return;
        }

        //安全刷新UI
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                //刷新UI(接受被观察者发过来的info)
                refreshDownLoadUI(downLoadInfo);
            }
        });
    }
}
