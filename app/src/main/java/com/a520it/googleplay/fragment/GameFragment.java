package com.a520it.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.ItemAdapter;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.factory.ListViewFactory;
import com.a520it.googleplay.holder.ItemHolder;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.protocol.GameProtocol;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:39
 * @desc ${TODD}
 */
public class GameFragment extends BaseFragment {


    private GameProtocol mProtocol;
    private List<ItemInfoBean> mData;
    private GameAdapter mAdapter;

    /**
     * 1. 解析json数据
     * 2. 根据返回的数据, 返回对应的状态
     * 3. 显示对应的界面
     *
     * @return
     */
    @Override
    public LoadingPager.LoadedResultState initData() {
        mProtocol = new GameProtocol();

        try {
            //解析json数据, 封装到指定对象中
            mData = mProtocol.loadData(0);

            //根据返回的数据, 返回对应的状态, 显示对应的界面
            return checkResData(mData);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultState.ERROR;
        }

    }

    @Override
    public View initSuccessView() {
        // 1. 创建listView
        ListView listView = ListViewFactory.createListView();

        // 2. 绑定适配器
        mAdapter = new GameAdapter(listView, mData);
        listView.setAdapter(mAdapter);
        return listView;
    }

    class GameAdapter extends ItemAdapter {

        /**
         * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
         *
         * @param absListView
         * @param datas
         */
        public GameAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

//        /**
//         * 初始化根视图
//         *
//         * @return
//         * @param position
//         */
//        @Override
//        public BaseHolder getSpecialHolder(int position) {
//
//            return new ItemHolder();
//        }
//
//        @Override
//        public boolean hasLoadMore() {
//            return true;
//        }

        @Override
        public List<ItemInfoBean> initLoadMoreData() throws Exception {
            SystemClock.sleep(1000);
            //读取指定分页的数据
            List<ItemInfoBean> itemInfoBeen = mProtocol.loadData(mData.size());
            return itemInfoBeen;
        }
    }

    /**
     * 失去焦点
     */
    @Override
    public void onPause() {
        super.onPause();
        //移除观察者
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;

            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().removeObserver(itemHolder);
            }
        }
    }

    /**
     * 获取焦点时, 添加观察, 并通知界面更新
     */
    @Override
    public void onResume() {
        super.onResume();
        //添加观察者
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().addObserver(itemHolder);
                //通知界面更新
                DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(itemHolder.mData);

                DownLoadManager.getInstance().notifyObservers(downLoadInfo);

            }




        }
    }
}
