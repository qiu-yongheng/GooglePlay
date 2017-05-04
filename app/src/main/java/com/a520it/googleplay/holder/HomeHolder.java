package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.StringUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

/**
 * @author 邱永恒
 * @time 2016/8/20  15:21
 * @desc 1.提供视图
 * @desc 2.接受数据
 * @desc 3.数据和视图的绑定
 */
public class HomeHolder extends BaseHolder<ItemInfoBean>{

    private ImageView appInfo_iv_icon;
    private RatingBar appInfo_rb_starts;
    private TextView appInfo_tv_des;
    private TextView appInfo_tv_size;
    private TextView appInfo_tv_title;

    /**
     * 给子控件设置值---->实现了BaseHolder的抽象方法
     * @param
     */
    @Override
    public void refreshHolderView(ItemInfoBean data) {
        //给子控件赋值
        appInfo_rb_starts.setRating(data.stars);
        appInfo_tv_des.setText(data.des);
        appInfo_tv_size.setText(StringUtils.formatFileSize(data.size));
        appInfo_tv_title.setText(data.name);

        //图片处理
        Picasso.with(UIUtils.getContext()).
                load(Constants.URLS.IMGBASEURL + data.iconUrl).//image?=iconUrl
                into(appInfo_iv_icon);

    }


    /**
     * 初始化根视图
     * @return
     */
    @Override
    public View initHolderView() {
        //初始化根视图
        mHolderView = View.inflate(UIUtils.getContext(), R.layout.item_home_info, null);

        //初始化子控件
        initView();


        return mHolderView;
    }

    /**
     * 初始化子控件
     */
    private void initView() {
        appInfo_iv_icon = (ImageView) mHolderView.findViewById(R.id.item_appinfo_iv_icon);
        appInfo_rb_starts = (RatingBar) mHolderView.findViewById(R.id.item_appinfo_rb_stars);
        appInfo_tv_des = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_des);
        appInfo_tv_size = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_size);
        appInfo_tv_title = (TextView) mHolderView.findViewById(R.id.item_appinfo_tv_title);
    }

}
