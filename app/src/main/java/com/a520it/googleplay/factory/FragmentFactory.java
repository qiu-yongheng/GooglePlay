package com.a520it.googleplay.factory;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.fragment.AppFragment;
import com.a520it.googleplay.fragment.CategoryFragment;
import com.a520it.googleplay.fragment.GameFragment;
import com.a520it.googleplay.fragment.HomeFragment;
import com.a520it.googleplay.fragment.HotFragment;
import com.a520it.googleplay.fragment.RecommendFragment;
import com.a520it.googleplay.fragment.SubjectFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邱永恒
 * @time 2016/8/19  14:40
 * @desc 生产fragment的工厂类
 */
public class FragmentFactory {
    public static final int FRAGMENT_HOME = 0;//首页
    public static final int FRAGMENT_APP = 1;//应用
    public static final int FRAGMENT_GAME = 2;//游戏
    public static final int FRAGMENT_SUBJECT = 3;//专题
    public static final int FRAGMENT_RECOMMEND = 4;//推荐
    public static final int FRAGMENT_CATEGORY = 5;//分类
    public static final int FRAGMENT_HOT = 6;//排行


    //把创建的Fragment存放在一个map集合中
    private static Map<Integer, BaseFragment> mFragmentMap = new HashMap<>();


    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = null;

        if (mFragmentMap.containsKey(position)) {
            //集合中有缓存, 取出
            fragment = mFragmentMap.get(position);
            return fragment;
        }

        //没有, 就创建
        switch (position) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_APP:
                fragment = new AppFragment();
                break;
            case FRAGMENT_GAME:
                fragment = new GameFragment();
                break;
            case FRAGMENT_SUBJECT:
                fragment = new SubjectFragment();
                break;
            case FRAGMENT_RECOMMEND:
                fragment = new RecommendFragment();
                break;
            case FRAGMENT_CATEGORY:
                fragment = new CategoryFragment();
                break;
            case FRAGMENT_HOT:
                fragment = new HotFragment();
                break;
        }
        //加入到集合中
        mFragmentMap.put(position, fragment);

        return fragment;
    }
}
