package com.a520it.googleplay.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 邱永恒
 * @time 2016/8/23  18:43
 * @desc ${TODD}
 */
public class ChildViewPager extends ViewPager{

    private float mStartX;
    private float mStartY;

    public ChildViewPager(Context context) {
        super(context);
    }

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                //获取当前手指坐标
                mStartX = ev.getRawX();
                mStartY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE://移动
                float newX = ev.getRawX();
                float newY = ev.getRawY();

                //获取移动的差值
                int diffX = (int) (newX - mStartX + .5f);
                int diffY = (int) (newY - mStartY + .5f);

                //判断是否是左右滑动
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;

            case MotionEvent.ACTION_UP://抬起
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(ev);
    }
}

