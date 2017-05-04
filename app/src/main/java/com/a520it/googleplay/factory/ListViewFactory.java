package com.a520it.googleplay.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.a520it.googleplay.utils.UIUtils;

/**
 * @author 邱永恒
 * @time 2016/8/22  20:21
 * @desc listView的工具类
 */
public class ListViewFactory {
    public static ListView createListView() {
        ListView listView = new ListView(UIUtils.getContext());

        //常规设置
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        listView.setFastScrollEnabled(true);
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        return listView;
    }
}
