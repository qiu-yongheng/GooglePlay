package com.a520it.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 邱永恒
 * @time 2016/8/19  19:13
 * @desc fragment基类
 */
public abstract class BaseFragmentCommon extends Fragment{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }


    /**
     * 初始化
     */
    public void init() {

    }

    /**
     * 初始化view
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 初始化监听
     */
    public void initListener() {

    }
}
