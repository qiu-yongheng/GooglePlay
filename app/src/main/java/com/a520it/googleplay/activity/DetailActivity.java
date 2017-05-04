package com.a520it.googleplay.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.holder.DetailBottomHolder;
import com.a520it.googleplay.holder.DetailDesHolder;
import com.a520it.googleplay.holder.DetailInfoHolder;
import com.a520it.googleplay.holder.DetailPicHolder;
import com.a520it.googleplay.holder.DetailSafeHolder;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.protocol.DetailProtocol;
import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/24  14:20
 * @desc ${TODD}
 */
public class DetailActivity extends ActionBarActivity {

    private String mPackName;
    private DetailProtocol mProtocol;
    private ItemInfoBean mItemInfoBean;
    private FrameLayout mFlBottom;
    private FrameLayout mFlDes;
    private FrameLayout mFlInfo;
    private FrameLayout mFlPic;
    private FrameLayout mFlSafe;
    private DetailBottomHolder mDetailBottomHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化标题栏和返回按钮
        initActionBar();
        //获取其他界面传过来的数据
        init();
        //初始化界面
        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        LoadingPager loadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            public LoadedResultState initData() {

                return DetailActivity.this.initData();
            }

            @Override
            public View initSuccessView() {
                View view = View.inflate(UIUtils.getContext(), R.layout.item_deital, null);

                //初始化控件
                mFlBottom = (FrameLayout) view.findViewById(R.id.detail_fl_bottom);
                mFlDes = (FrameLayout) view.findViewById(R.id.detail_fl_des);
                mFlInfo = (FrameLayout) view.findViewById(R.id.detail_fl_info);
                mFlPic = (FrameLayout) view.findViewById(R.id.detail_fl_pic);
                mFlSafe = (FrameLayout) view.findViewById(R.id.detail_fl_safe);


                //往应用的信息部分对应的FramentLayout填充数据
                DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
                mFlInfo.addView(detailInfoHolder.mHolderView);
                //把解析出来的数据传递过去
                detailInfoHolder.setDataAndRefreshHolderView(mItemInfoBean);

                //往应用的安全部分对应的FramentLayout填充数据
                DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
                mFlSafe.addView(detailSafeHolder.mHolderView);
                //把解析出来的数据传递过去
                detailSafeHolder.setDataAndRefreshHolderView(mItemInfoBean);

                //往应用的截图部分对应的FramentLayout填充数据
                DetailPicHolder detailPicHolder = new DetailPicHolder();
                mFlPic.addView(detailPicHolder.mHolderView);
                detailPicHolder.setDataAndRefreshHolderView(mItemInfoBean);

                //往应用的描述部分对应的FramentLayout填充数据
                DetailDesHolder detailDesHolder = new DetailDesHolder();
                mFlDes.addView(detailDesHolder.mHolderView);
                detailDesHolder.setDataAndRefreshHolderView(mItemInfoBean);

                //往应用的下载部分对应的FramentLayout填充数据
                mDetailBottomHolder = new DetailBottomHolder();

                //添加观察者到观察者集合中
                DownLoadManager.getInstance().addObserver(mDetailBottomHolder);


                mFlBottom.addView(mDetailBottomHolder.mHolderView);
                mDetailBottomHolder.setDataAndRefreshHolderView(mItemInfoBean);

                return view;
            }
        };
        //告知其具体展示的视图
        setContentView(loadingPager);

        //触发加载数据
        loadingPager.triggerLoadData();
    }

    /**
     * 真正的加载数据
     *
     * @return
     */
    private LoadingPager.LoadedResultState initData() {
        mProtocol = new DetailProtocol(mPackName);
        try {
            mItemInfoBean = mProtocol.loadData(0);
            if (mItemInfoBean == null) {
                return LoadingPager.LoadedResultState.EMPTY;
            }
            return LoadingPager.LoadedResultState.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultState.ERROR;
        }


    }

    /**
     * 获取其他界面传过来的数据
     */
    private void init() {
        mPackName = getIntent().getStringExtra(Constants.PACKAGENAME);
    }

    private void initActionBar() {
        ActionBar supporActionBar = getSupportActionBar();
        //设置状态栏title
        supporActionBar.setTitle("GooglePlay");
        //点击后返回主页
        supporActionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 失去焦点
     */
    @Override
    protected void onPause() {
        super.onPause();
        //移除观察者
        if (mDetailBottomHolder != null) {
            DownLoadManager.getInstance().removeObserver(mDetailBottomHolder);
        }
    }

    /**
     * 获取焦点
     */
    @Override
    protected void onResume() {
        super.onResume();
        //添加观察者
        if (mDetailBottomHolder != null) {
            DownLoadManager.getInstance().addObserver(mDetailBottomHolder);

            //手动发送最新状态
            DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mItemInfoBean);
            DownLoadManager.getInstance().notifyObservers(downLoadInfo);
        }
    }
}
