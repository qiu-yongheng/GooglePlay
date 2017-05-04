package com.a520it.googleplay.base;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.a520it.googleplay.activity.DetailActivity;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.holder.ItemHolder;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/24  13:47
 * @desc 首页, 应用, 游戏的公共的adapter
 */
public class ItemAdapter extends SuperBaseAdapter<ItemInfoBean>{
    /**
     * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
     *
     * @param absListView
     * @param datas
     */
    public ItemAdapter(AbsListView absListView, List datas) {
        super(absListView, datas);
    }


    //使用集合保存adapter中的itemholder(观察者)
    public List<ItemHolder> mItemHolders = new ArrayList<>();

    @Override
    public BaseHolder getSpecialHolder(int position) {
        ItemHolder itemHolder =  new ItemHolder();

        //添加观察者到观察者集合
        DownLoadManager.getInstance().addObserver(itemHolder);

        //添加观察者到集合中
        mItemHolders.add(itemHolder);

        return itemHolder;
    }

    @Override
    public boolean hasLoadMore() {
        return true;
    }


    @Override
    public void onNomalItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(UIUtils.getContext(), ((ItemInfoBean)mDataSet.get(position)).packageName, Toast.LENGTH_SHORT).show();

        //跳转到详情页面
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);

        //传包名给详情页
        intent.putExtra(Constants.PACKAGENAME, ((ItemInfoBean)mDataSet.get(position)).packageName);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        UIUtils.getContext().startActivity(intent);

        super.onNomalItemClick(parent, view, position, id);
    }


}
