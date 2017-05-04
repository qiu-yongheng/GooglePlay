package com.a520it.www.iconveiw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 邱永恒
 * @time 2016/8/26  10:13
 * @desc ${TODD}
 */
public class ProgressView extends LinearLayout{

    private ImageView mIv;
    private TextView mTv;

    private long mMax = 100;
    private long mProgress;
    private boolean isProgressEnable = true;

    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置当前进度
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;
        //调用onDraw方法
        invalidate();
    }

    public void setProgressEnable(boolean progressEnable) {
        isProgressEnable = progressEnable;
    }

    public void setIv(int resId) {
        mIv.setImageResource(resId);
    }

    public void setTv(String note) {
        mTv.setText(note);
    }

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //挂载定义的布局(挂载到当前布局)
        View.inflate(context, R.layout.item_icon, this);

        mIv = (ImageView) findViewById(R.id.iv_icon);
        mTv = (TextView) findViewById(R.id.tv);
    }

    /**
     * 主要用来画背景
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //画圆弧
        //定义圆弧的位置
        RectF rectF = new RectF(mIv.getLeft(), mIv.getTop(), mIv.getRight(), mIv.getBottom());

        float startAngle = -90;

        //转过的角度
        float sweepAngle = mProgress * 1.0f / mMax * 360 + .5f;
        boolean useCenter = false;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);

        canvas.drawArc(rectF, startAngle, sweepAngle, useCenter, paint);
    }
}
