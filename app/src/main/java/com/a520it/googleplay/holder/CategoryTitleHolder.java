package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.CategoryInfoBean;
import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/23  21:36
 * @desc ${TODD}
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

    private TextView mTv;

    @Override
    public void refreshHolderView(CategoryInfoBean data) {
        mTv.setText(data.title);
    }

    @Override
    public View initHolderView() {
        mTv = new TextView(UIUtils.getContext());

        int padding = UIUtils.dip2Px(5);
        mTv.setPadding(padding, padding, padding, padding);

        return mTv;
    }
}
