package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/21  12:08
 * @desc 加载更多的holder
 */
public class LoadMoreHolder extends BaseHolder<Integer>{
    //定义几种状态
    public static final int LOADMORE_LOADING = 0;//正在加载
    public static final int LOADMORE_ERROR = 1;//加载失败
    public static final int LOADMORE_NONE = 2;//没有更多





    private LinearLayout item_loading_ll;
    private LinearLayout item_retry_ll;

    /**
     * 给布局文件赋值
     * @param state
     */
    @Override
    public void refreshHolderView(Integer state) {
        //1. 隐藏所有视图
        item_loading_ll.setVisibility(View.GONE);
        item_retry_ll.setVisibility(View.GONE);

        //2. 根据加载状态, 显示视图
        switch (state) {
            case LOADMORE_LOADING:
                item_loading_ll.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_ERROR:
                item_retry_ll.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_NONE:
                //没有更多
                break;
        }
    }

    /**
     * 初始化布局
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);

        //初始化子控件
        initView(view);
        return view;
    }

    private void initView(View view) {
        item_loading_ll = (LinearLayout) view.findViewById(R.id.item_loadmore_container_loading);
        item_retry_ll = (LinearLayout) view.findViewById(R.id.item_loadmore_container_retry);
    }

}
