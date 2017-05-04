package com.a520it.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a520it.googleplay.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * @author 邱永恒
 * @time 2016/8/19  19:26
 * @desc 对Fragment的抽取封装
 */
public abstract class BaseFragment extends Fragment{

    public LoadingPager mLoadingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 子类实现父类的抽象方法
         * @return
         */
        if (mLoadingPager == null) {
            //由继承的子类初始化具体的成功的视图
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {
                /**
                 * 子类实现父类的抽象方法
                 * @return
                 */
                @Override
                public LoadedResultState initData() {
                    //由继承的子类加载数据
                    return BaseFragment.this.initData();
                }


                @Override
                public View initSuccessView() {
                    //由继承的子类初始化具体的成功的视图
                    return BaseFragment.this.initSuccessView();
                }
            };

        }

        //在这里触发数据, 会发生预加载数据
        //触发加载数据(然后更新UI)
//        mLoadingPager.triggerLoadData();
        return mLoadingPager;
    }




    // 数据加载的流程
    /**
     ① 触发加载      进入页面开始加载/点击某一个按钮的时候加载
     ② 异步加载数据  -->显示加载视图
     ③ 处理加载结果
     ① 成功-->显示成功视图
     ② 失败
     ① 数据为空-->显示空视图
     ② 数据加载失败-->显示加载失败的视图
     */




    public abstract LoadingPager.LoadedResultState initData();

    public abstract View initSuccessView();


    /**
     * 根据返回的数据, 返回具体的状态
     * @param resObj
     * @return
     */
    public LoadingPager.LoadedResultState checkResData(Object resObj) {
        //数据不存在
        if (resObj == null) {
            return LoadingPager.LoadedResultState.EMPTY;
        }

        //没有数据
        if (resObj instanceof List) {
            if (((List) resObj).size() == 0) {
                return LoadingPager.LoadedResultState.EMPTY;
            }
        }

        //
        if (resObj instanceof Map) {
            if (((Map) resObj).size() == 0) {
                return LoadingPager.LoadedResultState.EMPTY;
            }
        }

        return LoadingPager.LoadedResultState.SUCCESS;
    }
}
