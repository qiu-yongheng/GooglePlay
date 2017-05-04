package com.a520it.googleplay.holder;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.UIUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/24  16:10
 * @desc ${TODD}
 */
public class DetailSafeHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {
    //默认显示打开, 点击后关闭
    private boolean isOpen = true;


    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;
    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;
    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        List<ItemInfoBean.ItemInfoSafeBean> safe = data.safe;

        //遍历集合, 查看集合中有多少个图片路径, 分别加载
        for (ItemInfoBean.ItemInfoSafeBean info : safe) {
            String safeUrl = info.safeUrl;

            ImageView ivIcon = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + safeUrl).into(ivIcon);

            //把图片控件设置到父控件中
            mAppDetailSafePicContainer.addView(ivIcon);

            //创建一个线性布局
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());

            //设置单选框
            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + info.safeDesUrl).into(ivDesIcon);

            //设置文本
            TextView tvDes = new TextView(UIUtils.getContext());
            tvDes.setText(info.safeDes);

            //设置文本颜色
            if (info.safeDesColor == 0) {
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                //警告色
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }

            //设置显示间距
            int padding = UIUtils.dip2Px(4);
            linearLayout.setPadding(padding, padding, padding, padding);

            linearLayout.addView(ivDesIcon);
            linearLayout.addView(tvDes);

            mAppDetailSafeDesContainer.addView(linearLayout);
        }

        //默认折叠, 不带动画
        toggleAnimation(false);
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_deital_safe, null);
        ButterKnife.bind(DetailSafeHolder.this, view);

        //设置点击事件
        view.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        //点击的动画
        toggleAnimation(true);
    }

    private void toggleAnimation(boolean isAnimation) {
        if (isOpen) {
            //点击后折叠(高度变成0)
            mAppDetailSafeDesContainer.measure(0, 0);

            //获取当前的高度
            int measuredHeight = mAppDetailSafeDesContainer.getMeasuredHeight();

            int start = measuredHeight;
            int end = 0;

            //判断是否显示动画
            if (isAnimation) {
                //添加动画
                doAnimation(start, end);
            } else {
                //直接修改高度

                //获取布局的参数
                ViewGroup.LayoutParams params = mAppDetailSafeDesContainer.getLayoutParams();

                //修改布局的高度
                params.height = end;

                mAppDetailSafeDesContainer.setLayoutParams(params);
            }




        } else {
            //展开
            //激活系统的测量的逻辑(系统不关心设置的值)
            mAppDetailSafeDesContainer.measure(0, 0);

            //获取当前的高度
            int measuredHeight = mAppDetailSafeDesContainer.getMeasuredHeight();

            int start = 0;
            int end = measuredHeight;

            //判断是否显示动画
            if (isAnimation) {
                //添加动画
                doAnimation(start, end);
            } else {
                //获取布局参数
                ViewGroup.LayoutParams params = mAppDetailSafeDesContainer.getLayoutParams();
                //直接修改高度
                params.height = end;

                mAppDetailSafeDesContainer.setLayoutParams(params);
            }


        }

        //改变标签
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {
        //创建一个第三方动画
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        //启动动画
        animator.start();


        //得到动画中的渐变值
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //渐变值
                int height = (int) animation.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = height;

                //设置最新的layoutParams信息
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);

            }
        });

        //箭头跟着动
        if (isOpen) {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0, 180).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180, 0).start();
        }
    }
}
