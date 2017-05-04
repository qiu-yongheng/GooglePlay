package com.a520it.googleplay.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.a520it.googleplay.R;

/**
 * @author 邱永恒
 * @time 2016/8/23  19:38
 * @desc 1. 已知高度(精确), 可以动态计算宽度
 * @desc 2. 已知宽度(精确), 可以动态计算高度
 */
public class RatilLayout extends FrameLayout {
    public static final int RELATIVE_WIDTH = 0;//已知宽度, 可以动态计算高度
    public static final int RECATIVE_HEIGHT = 1;//已知高度, 可以动态计算宽度
    //设置图片的宽高比
    private float mPicRatio = 2.43f;

    //设置默认值
    private int mRelative = RELATIVE_WIDTH;

    public RatilLayout(Context context) {
        super(context);
    }

    public RatilLayout(Context context, AttributeSet attrs) {
        super(context, attrs);


        /**
         * 获取自定义的属性
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatilLayout);

        mRelative = typedArray.getInt(R.styleable.RatilLayout_relative, 0);
        mPicRatio = typedArray.getFloat(R.styleable.RatilLayout_picRatio, 1);

        typedArray.recycle();
    }


    /**
            * 在代码中设置比例
    *
            * @param ratio
    */
    public void setRatio(float ratio) {
        this.mPicRatio = ratio;
    }

    /**
     * 在代码中设置参考
     *
     * @param relative
     */
    public void setRelative(int relative) {
        if (relative > 1 || relative < 0) {
            return;
        }
        this.mRelative = relative;
    }


    /**
     * 设置父容器的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 1. UNSPECIFIED 不确定的 wrap_content
         * 2. EXACTLY 确定 match_parent
         * 3. AT_MOST 至多
         */


        //得到在xml中定义的width的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //得到在xml中定义的height的模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //宽度确定
        if (widthMode == MeasureSpec.EXACTLY && mRelative == RELATIVE_WIDTH && mPicRatio != 0) {
            // 1. 获取父控件的宽度
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

            // 2. 动态计算出父控件的高度(比例)
            int parentHeight = (int) (parentWidth / mPicRatio + .5f);

            // 3. 计算子控件的高度和宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();

            // 4. 测绘子控件
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 5. 设置父容器的大小
            setMeasuredDimension(parentWidth, parentHeight);


        } else if (heightMode == MeasureSpec.EXACTLY && mRelative == RECATIVE_HEIGHT && mPicRatio != 0) {


            // 1. 获取父控件的高度
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

            // 2. 动态计算出父控件的宽度(比例)
            int parentWidth = (int) (parentHeight * mPicRatio + .5f);

            // 3. 计算子控件的高度和宽度
            int childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            int childHeight = parentHeight - getPaddingTop() - getPaddingBottom();

            // 4. 测绘子控件
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

            // 5. 设置父容器的大小
            setMeasuredDimension(parentWidth, parentHeight);

        } else {

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
