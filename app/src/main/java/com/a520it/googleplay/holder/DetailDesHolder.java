package com.a520it.googleplay.holder;

import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/24  16:10
 * @desc ${TODD}
 */
public class DetailDesHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {
    public boolean isOpen = true;

    @Bind(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;
    @Bind(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;
    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;
    private int mAppDetailDesTvHeight;
    private ItemInfoBean mItemInfoBean;

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mItemInfoBean = data;


        mAppDetailDesTvAuthor.setText(data.author);
        mAppDetailDesTvDes.setText(data.des);

        //监听描述详情的textview布局完成(渲染树)
        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前的高度
                mAppDetailDesTvHeight = mAppDetailDesTvDes.getMeasuredHeight();
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //默认折叠
                toggleAnimation(false);
            }
        });
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
        ButterKnife.bind(this, view);

        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        toggleAnimation(true);

    }

    private void toggleAnimation(boolean isAnimation) {
        if (isOpen) {
            //默认折叠--->折叠到7行的高度
            int start = mAppDetailDesTvHeight;
            int end = getLineHeight(7, mItemInfoBean.des);

            //第一次进来不设置动画
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }

        } else {
            //展开
            int start = getLineHeight(7, mItemInfoBean.des);
            int end = mAppDetailDesTvHeight;

            //第一次进来不设置动画
            if (isAnimation) {
                doAnimation(start, end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }

        }
        //改变标签
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mAppDetailDesTvDes, "height", start, end);
        animator.start();


        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 0, 180).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 180, 0).start();
        }

        //监听动画执行过程
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {//动画结束
                //找到scrollView, 然后进行滚动
                ViewParent parent = mAppDetailDesTvDes.getParent();
                //一层层往下找
                while (true) {
                    //第二级容器
                    parent = parent.getParent();
                    if (parent instanceof ScrollView) {
                        //开始滚动效果
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }
                    if (parent == null) {
                        break;
                    }

                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    /**
     * 返回具体的textview的应有高度
     *
     * @param line    textview行高
     * @param content textview里面显示的内容
     * @return
     */
    private int getLineHeight(int line, String content) {
        TextView tempTv = new TextView(UIUtils.getContext());
        tempTv.setText(content);
        tempTv.setLines(line);

        //指定行数后, 拿到指定行数的高度
        tempTv.measure(0, 0);
        return tempTv.getMeasuredHeight();
    }
}
