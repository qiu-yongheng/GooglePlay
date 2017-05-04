package com.a520it.googleplay.base;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/20  13:35
 * @desc BaseAdapter的简单抽取
 */
public abstract class MyBaseAdapter<ITEMBEANTYPE> extends BaseAdapter{
    //泛型
    public List<ITEMBEANTYPE> mDataSet = new ArrayList<>();

    //获取外界传入的数据
    public MyBaseAdapter(List<ITEMBEANTYPE> datas) {
        mDataSet = datas;
    }

    @Override
    public int getCount() {
        if (mDataSet != null) {
            return mDataSet.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDataSet != null) {
            return mDataSet.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
