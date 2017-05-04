package com.a520it.googleplay.base;

import android.view.View;

/**
 * @author 邱永恒
 * @time 2016/8/20  17:23
 * @desc 初始化根视图的类
 */
public abstract class BaseHolder<HOLDERBEANTYPE> {
    //获取要绑定的根视图
    public View mHolderView;

    public HOLDERBEANTYPE mData;


    /**-----------1. 获取根视图-----------**/
    public BaseHolder() {
        //初始化根视图, 控件(交给子类去实现)
        mHolderView = initHolderView();

        //设置标签(父类来实现)
        mHolderView.setTag(this);
    }



    /**-----------2. 接收数据, 给子控件绑定数据-----------**/
    public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
        //保存数据到成员变量
        mData = data;

        //给子控件绑定数据, 只能给子类实现
        refreshHolderView(mData);
    }






    /**
     * 由子类去实现---->HomeHolder
     * @param data
     */
    //给控件加载数据(给子类调用的时候实现)
    public abstract void refreshHolderView(HOLDERBEANTYPE data);




    //初始化根视图(给子类调用的时候实现)
    public abstract View initHolderView();
}
