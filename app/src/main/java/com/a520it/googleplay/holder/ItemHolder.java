package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.utils.CommonUtils;
import com.a520it.googleplay.utils.StringUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.a520it.googleplay.views.ProgressView;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * @author 邱永恒
 * @time 2016/8/22  20:41
 * @desc ${TODD}
 */
public class ItemHolder extends BaseHolder<ItemInfoBean> implements DownLoadManager.DownLoadInfoObserver, View.OnClickListener {

    private ImageView appInfo_iv_icon;
    private RatingBar appInfo_rb_starts;
    private TextView appInfo_tv_des;
    private TextView appInfo_tv_size;
    private TextView appInfo_tv_title;
    private ProgressView mProgressView;
    public ItemInfoBean mData;

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mData = data;

        //给子控件赋值
        appInfo_rb_starts.setRating(data.stars);
        appInfo_tv_des.setText(data.des);
        appInfo_tv_size.setText(StringUtils.formatFileSize(data.size));
        appInfo_tv_title.setText(data.name);

        //图片处理
        Picasso.with(UIUtils.getContext()).
                load(Constants.URLS.IMGBASEURL + data.iconUrl).//image?=iconUrl
                into(appInfo_iv_icon);


        /**------------修改下载按钮的UI------------**/
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mData);
        refreshProgressButtonUi(downLoadInfo);
    }

    /**
     * 根据downLoadInfo里面的状态刷新下载按钮的对应的UI
     *
     * @param downLoadInfo
     */
    private void refreshProgressButtonUi(DownLoadInfo downLoadInfo) {
        //获取当前的下载状态
        int cuurState = downLoadInfo.curState;


        switch (cuurState) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mProgressView.setTv("点我下载");
                break;

            case DownLoadManager.STATE_DOWNLOADING://下载中
                //设置最大进度
                mProgressView.setMax(downLoadInfo.max);

                //设置当前进度
                mProgressView.setProgress(downLoadInfo.progress);

                //获取当前进度
                int progress = (int) (downLoadInfo.progress * 1.0f / downLoadInfo.max * 100 + .5f);

                mProgressView.setProgressEnable(true);

                mProgressView.setTv(progress + "%");
                break;

            case DownLoadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mProgressView.setIv(R.drawable.ic_resume);

                mProgressView.setTv("继续下载");
                break;

            case DownLoadManager.STATE_WAITINGDOWNLOAD://等待下载
                mProgressView.setIv(R.drawable.ic_pause);

                mProgressView.setTv("等待中...");
                break;

            case DownLoadManager.STATE_DOWNLOADFALSED://下载失败
                mProgressView.setIv(R.drawable.ic_redownload);

                mProgressView.setTv("重试");
                break;

            case DownLoadManager.STATE_DOWNLOADED://下载完成
                mProgressView.setProgressEnable(false);

                mProgressView.setTv("安装");
                break;

            case DownLoadManager.STATE_INSTALLED://已安装
                mProgressView.setTv("打开");
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

    @Override
    public View initHolderView() {
        //初始化根视图
        mHolderView = View.inflate(UIUtils.getContext(), R.layout.item_home_info, null);

        //初始化子控件
        initView();


        return mHolderView;
    }


    /**
     * 初始化子控件
     */
    private void initView() {
        appInfo_iv_icon = (ImageView) mHolderView.findViewById(R.id.item_appinfo_iv_icon);
        appInfo_rb_starts = (RatingBar) mHolderView.findViewById(R.id.item_appinfo_rb_stars);
        appInfo_tv_des = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_des);
        appInfo_tv_size = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_size);
        appInfo_tv_title = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_title);
        mProgressView = (ProgressView) mHolderView.findViewById(R.id.item_appinfo_progressview);
        mProgressView.setOnClickListener(this);
    }


    @Override
    public void onDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        //过滤观察者
        if (!downLoadInfo.packageName.equals(mData.packageName)) {
            //如果返回的应用的包名和当前应用的包名不同, 直接返回
            return;
        }

        //安全刷新UI
        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                //刷新UI(接受被观察者发过来的info)
                refreshProgressButtonUi(downLoadInfo);
            }
        });
    }

    @Override
    public void onClick(View v) {

        DownLoadInfo info = DownLoadManager.getInstance().getDownLoadInfo(mData);
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
}
