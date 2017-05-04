package com.a520it.googleplay.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * @author 邱永恒
 * @time 2016/8/25  13:39
 * @desc 带有进度的按钮
 */
public class ProgressButton extends Button {

    private long mMax;
    private long mProgress;
    private boolean isProgressEnable;

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isProgressEnable) {
            ColorDrawable drawable = new ColorDrawable(Color.BLUE);

            //设置绘制背景的大小
            int left = 0;
            int top = 0;
            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);
            int bottom = getBottom();

            //设置绘制范围
            drawable.setBounds(left, top, right, bottom);

            //绘制到画布上
            drawable.draw(canvas);


        }

        //防止覆盖父类绘制的内容
        super.onDraw(canvas);
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;

        //动态设置进度, 重绘
        invalidate();
    }

    /**
     * 设置是否允许progress
     *
     * @param isProgressEnable
     */
    public void setIsProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }
}
