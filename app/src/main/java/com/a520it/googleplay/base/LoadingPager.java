package com.a520it.googleplay.base;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.a520it.googleplay.R;
import com.a520it.googleplay.factory.ThreadPoolProxyFactory;
import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/19  19:29
 * @desc 1. 提供视图(4种视图类型中的一种)  2. 触发加载数据(绑定具体的视图)
 */
public abstract class LoadingPager extends FrameLayout {
    //声明视图
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    public static final int STATE_LOADING = 0;//展示加载中的视图
    public static final int STATE_EMPTY = 1;//展示空视图
    public static final int STATE_ERROR = 2;//展示错误视图
    public static final int STATE_SUCCESS = 3;//展示成功视图

    public int mCurState = STATE_LOADING;//默认展示加载中的视图
    private LoadDataTask mLoadDataTask;


    /**
     * 任何应用其实就只有4种页面类型
     * ① 加载页面
     * ② 错误页面
     * ③ 空页面
     * ④ 成功页面
     * ①②③三种页面一个应用基本是固定的
     * 每一个fragment对应的页面④就不一样
     * 进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */
    public LoadingPager(Context context) {
        super(context);
        //初始化常规视图(加载页面, 错误页面, 空页面)
        initCommonView();
    }

    /**
     * 初始化常规视图(加载页面, 错误页面, 空页面)
     */
    private void initCommonView() {
        //加载界面
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);

        //错误界面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        this.addView(mErrorView);

        //空界面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);

        //根据不同的状态显示不同的视图
        refreshViewByState();

        //加载数据
        triggerLoadData();

    }

    /**
     * 根据当前的类型, 展示不同的视图类型
     */
    private void refreshViewByState() {
        //控制加载中的视图的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING) ? View.VISIBLE : View.GONE);

        //控制错误视图的显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        mErrorView.findViewById(R.id.btn_error_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新加载数据
                triggerLoadData();
            }
        });



        //控制空视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        //数据加载成功&&当前的成功视图是空
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            //设置成功视图
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
        }

        //控制成功视图的显示
        if (mSuccessView != null) {
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS)? View.VISIBLE: View.GONE);
        }
    }


    /**
     * 触发加载数据的方法
     */
    public void triggerLoadData() {

        //数据没有加载成功,&& 没有正在加载的数据 (继续加载数据)
        if (mCurState != STATE_SUCCESS && mLoadDataTask == null) {
            //一开始加载, 就重置当前状态
            mCurState = STATE_LOADING;
            //刷新UI
            refreshViewByState();


            //异步加载数据
            mLoadDataTask = new LoadDataTask();
//            new Thread(mLoadDataTask).start();
            //使用线程池中的线程执行任务
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(mLoadDataTask);
        }

    }

    class LoadDataTask implements Runnable {

        @Override
        public void run() {
            Log.v("gaga", "进来了");
            //在子线程中加载数据, 得到数据
            LoadedResultState loadedResultState = initData();

            //根据获取数据后返回的状态, 更新界面
            mCurState = loadedResultState.getState();

            //安全的更新UI
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    //更新UI
                    refreshViewByState();
                }
            });
            //一个任务执行完成, 把任务置空
            mLoadDataTask = null;
        }
    }

    /**
     * 由子类加载数据
     * 交给子类具体实现
     * @return 返回枚举
     */
    public abstract LoadedResultState initData();

    /**
     *
     * @return
     */
    public abstract View initSuccessView();


    public enum LoadedResultState {
        SUCCESS(STATE_SUCCESS), ERROR(STATE_ERROR), EMPTY(STATE_EMPTY);

        int state;

        public int getState() {
            return state;
        }

        private LoadedResultState(int state) {
            this.state = state;
        }
    }
}
