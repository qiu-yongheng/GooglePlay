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
import com.a520it.googleplay.protocol.AppProtocol;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:39
 * @desc ${TODD}
 */
public class AppFragment extends BaseFragment {

    private List<ItemInfoBean> mData;
    private AppProtocol mProtocol;
    private AppAdapter mAdapter;

    @Override
    public LoadingPager.LoadedResultState initData() {
        mProtocol = new AppProtocol();

        try {

            //解析json数据到指定对象中
            mData = mProtocol.loadData(0);

            //根据返回的数据, 返回具体的状态
            return checkResData(mData);

        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultState.ERROR;
        }

    }

    @Override
    public View initSuccessView() {
        //创建listView
        ListView listView = ListViewFactory.createListView();

        //绑定适配器
        mAdapter = new AppAdapter(listView, mData);
        listView.setAdapter(mAdapter);

        return listView;
    }

    class AppAdapter extends ItemAdapter {

        /**
         * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
         *
         * @param absListView
         * @param datas
         */
        public AppAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

//        /**
//         * 初始化根视图, 得到BaseHolder的具体的子类对象(data+view)
//         * @return
//         * @param position
//         */
//        @Override
//        public BaseHolder getSpecialHolder(int position) {
//            //初始化当前视图
//            return new ItemHolder();
//        }

//        /**
//         * 处理加载更多
//         * @return
//         */
//        @Override
//        public boolean hasLoadMore() {
//            return true;
//        }

        /**
         * 在子线程中真正的加载更多数据
         * @return
         * @throws Exception
         */
        @Override
        public List initLoadMoreData() throws Exception {
            SystemClock.sleep(1000);
            //获取当前分页的索引, 解析之后的json数据, 保存到指定对象中
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
