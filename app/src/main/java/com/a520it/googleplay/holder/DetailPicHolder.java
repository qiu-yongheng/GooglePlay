package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.UIUtils;
import com.a520it.googleplay.views.RatilLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/24  16:10
 * @desc ${TODD}
 */
public class DetailPicHolder extends BaseHolder<ItemInfoBean> {
    @Bind(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;
    private TextView mTv;

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        List<String> screenList = data.screen;
        for (int i = 0; i < screenList.size(); i++) {
            // 1. 获取图片
            String screen = screenList.get(i);

            //创建图片控件, 加载图片
            ImageView iv = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + screen).into(iv);


            //设置图片等比例显示
            RatilLayout rl = new RatilLayout(UIUtils.getContext());

            //已知宽度, 动态计算高度
            rl.setRelative(RatilLayout.RELATIVE_WIDTH);

            //设置图片比例
            rl.setRatio((float) 150 / 250);

            //把iv包裹在指定大小的容器中
            rl.addView(iv);


            //获取屏幕的宽度
            int screenWidth = UIUtils.getResources().getDisplayMetrics().widthPixels;

            //同屏只显示3张图片
            screenWidth = screenWidth - UIUtils.dip2Px(12);//减去图片之间的间距
            screenWidth = screenWidth / 3;

            //
            int width = screenWidth;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

            //设置左边距
            if (i != 0) {
                //第一张不设置间距
                params.leftMargin = UIUtils.dip2Px(3);
            }
            //添加控件到父容器中
            mAppDetailPicIvContainer.addView(rl, params);

        }
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_deital_pic, null);
        ButterKnife.bind(this, view);

        return view;
    }
}
