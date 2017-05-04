package com.a520it.googleplay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.factory.FragmentFactory;
import com.a520it.googleplay.holder.MenuLeftHolder;
import com.a520it.googleplay.utils.UIUtils;
import com.astuetz.PagerSlidingTabStripMy;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.app.ActionBar mActionBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ViewPager mViewPager;
    private PagerSlidingTabStripMy mTabs;
    private String[] mMainTitleArr;
    private FrameLayout main_menu_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置标题栏
        initActionBar();

        //初始化控件
        initView();

        //初始化要显示的数据
        initData();
    }

    /**
     * 设置标题栏
     */
    private void initActionBar() {
        // 获取ActionBar
        mActionBar = getSupportActionBar();

        mActionBar.setTitle("MainTitle");// 设置主title部分
        mActionBar.setDisplayHomeAsUpEnabled(true);// 设置back按钮是否可见
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);

        //设置回退控件的切换效果
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        //1. 同步状态
        mToggle.syncState();
        //2. 对mDrawerLayout设置监听
        mDrawerLayout.setDrawerListener(mToggle);

        //初始化主界面控件
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        mTabs = (PagerSlidingTabStripMy) findViewById(R.id.main_tabs);



        //找到左边显示的framelayout
        MenuLeftHolder menuLeftHolder = new MenuLeftHolder();
        menuLeftHolder.setDataAndRefreshHolderView(null);

        main_menu_left = (FrameLayout) findViewById(R.id.main_menu_left);
        main_menu_left.addView(menuLeftHolder.mHolderView);
    }

    /**
     * 初始化要显示的数据
     */
    private void initData() {
        //获取String.xml中的字符数组
        mMainTitleArr = UIUtils.getStringArr(R.array.main_titles);

        //viewPager绑定Adapter
        mViewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));

        //绑定viewpager
        mTabs.setViewPager(mViewPager);


        //设置ViewPager切换的监听(切换触发获取数据)
        final MyOnPageChangeListener listener = new MyOnPageChangeListener();
        mTabs.setOnPageChangeListener(listener);

        //手动触发选中第一页
        //监听渲染完毕后, 再设置默认选中
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //渲染完毕后, 再设置默认选中
                listener.onPageSelected(0);
                //移除监听器
                mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //动态的创建Fragment
            BaseFragment fragment = FragmentFactory.createFragment(position);

            //触发加载数据
            fragment.mLoadingPager.triggerLoadData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mToggle.onOptionsItemSelected(item);
                break;
        }
        return true;
    }


    /**
     * FragmentStatePagerAdapter --> 切换到下一个fragment, 清除上一个fragment
     * FragmentPagerAdapter --> 切换到下一个fragment, 缓存上一个fragment
     */
    class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            if (mMainTitleArr != null) {
                return mMainTitleArr.length;
            }
            return 0;
        }

        /**
         * 必须重写这个方法, 不然会报空指针
         * 获取页面的标题
         *
         * @param position
         * @return 字符串
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitleArr[position];
        }
    }


}
