package com.a520it.googleplay.holder;

import android.view.View;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/27  11:28
 * @desc ${TODD}
 */
public class MenuLeftHolder extends BaseHolder<Object>{
    @Override
    public void refreshHolderView(Object data) {

    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_menu_left, null);
        return view;
    }
}
