package com.a520it.googleplay.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.LogUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/22  21:55
 * @desc 初始化视图, 绑定数据
 */
public class HomePictureHolder extends BaseHolder<List<String>> implements ViewPager.OnPageChangeListener {

    private LinearLayout item_home_picture_container_indicator;
    private ViewPager item_home_picture_pager;
    private List<String> mPictures;

    /**
     * 给子控件设置值
     *
     * @param data 图片地址
     */
    @Override
    public void refreshHolderView(List<String> data) {
        //保存数据为成员变量
        mPictures = data;

        //给listView设置adapter
        item_home_picture_pager.setAdapter(new HomePictureAdapter());

        //处理提示小圆圈的显示
        for (int i = 0; i < mPictures.size(); i++) {
            ImageView ivIndicator = new ImageView(UIUtils.getContext());

            ivIndicator.setImageResource(R.drawable.indicator_normal);

            //默认选中第一个
            if (i == 0) {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }

            int width = UIUtils.dip2Px(5);
            int height = UIUtils.dip2Px(5);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

            params.leftMargin = UIUtils.dip2Px(5);
            params.bottomMargin = UIUtils.dip2Px(5);

            item_home_picture_container_indicator.addView(ivIndicator, params);
        }

        //滑动的数据切换indicator
        item_home_picture_pager.setOnPageChangeListener(this);





        //无限轮播
        int diff = Integer.MAX_VALUE / 2 % mPictures.size();//偏差值
        int index = Integer.MAX_VALUE / 2 - diff;

        //设置当前的显示的页面
        item_home_picture_pager.setCurrentItem(index);

        //设置自动轮播
        final AutoScrollTask autoScrollTask = new AutoScrollTask();
        autoScrollTask.start();


        //设置按下后停止轮播(触摸事件)
        item_home_picture_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下
                        LogUtils.v("gaga", "停止任务");
                        autoScrollTask.stop();//停止任务
                        break;

                    case MotionEvent.ACTION_MOVE://手指移动
                        
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL://取消
                        LogUtils.v("gaga", "start task");
                        autoScrollTask.start();//启动任务
                        break;
                }
                return false;
            }
        });

    }


    class AutoScrollTask implements Runnable {
        /**
         * 开始定时任务
         */
        public void start() {
            UIUtils.getHandler().postDelayed(this, 3000);
        }

        /**
         * 移除定时任务
         */
        public void stop() {
            UIUtils.getHandler().removeCallbacks(this);
        }


        @Override
        public void run() {
            //获取当前显示的item的索引
            int currentItem = item_home_picture_pager.getCurrentItem();

            LogUtils.v("av", currentItem + "");

            currentItem++;

            //自增后, 设置显示的页面
            item_home_picture_pager.setCurrentItem(currentItem);

            //启动定时循环任务
            start();
        }
    }

    /**
     * 初始化控件
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);

        //初始化控件
        initView(view);
        return view;
    }

    private void initView(View view) {
        item_home_picture_container_indicator = (LinearLayout) view.findViewById(R.id.item_home_picture_container_indicator);
        item_home_picture_pager = (ViewPager) view.findViewById(R.id.item_home_picture_pager);
    }




    class HomePictureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPictures != null) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 往viewPager中添加控件
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //防止索引越界
            position = position % mPictures.size();


            ImageView iv = new ImageView(UIUtils.getContext());

            //设置全屏显示
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            //获取图片地址
            String url = mPictures.get(position);

            //加载图片到图片控件中
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);

            LogUtils.v("gaga", Constants.URLS.IMGBASEURL + url);

            //添加图片控件到容器中
            container.addView(iv);
            return iv;
        }


        /**
         * 往viewPager中移除控件
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position % mPictures.size();

        // 选中特定的圆点
        for (int i = 0; i < mPictures.size(); i++) {
            // 获取容器中的指定索引的子控件
            ImageView ivIndicator = (ImageView) item_home_picture_container_indicator.getChildAt(i);

            // 将所有页面初始化为非选中状态
            ivIndicator.setImageResource(R.drawable.indicator_normal);

            // 设置选中的圆点
            if (i == position) {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
