package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.SubjectInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/23  19:16
 * @desc ${TODD}
 */
public class SubjectHolder extends BaseHolder<SubjectInfoBean> {
    @Bind(R.id.item_subject_iv_icon)
    ImageView mItemSubjectIvIcon;
    @Bind(R.id.item_subject_tv_title)
    TextView mItemSubjectTvTitle;

    /**
     * 给控件设置值
     *
     * @param data
     */
    @Override
    public void refreshHolderView(SubjectInfoBean data) {
        mItemSubjectTvTitle.setText(data.des);

        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url).into(mItemSubjectIvIcon);
    }

    /**
     * 初始化视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);

        //找到子控件, 变成成员变量
        ButterKnife.bind(this, view);
        return view;
    }
}
