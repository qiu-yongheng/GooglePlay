package com.a520it.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.ItemAdapter;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.bean.DownLoadInfo;
import com.a520it.googleplay.bean.HomeInfoBean;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.factory.ListViewFactory;
import com.a520it.googleplay.holder.HomePictureHolder;
import com.a520it.googleplay.holder.ItemHolder;
import com.a520it.googleplay.manager.DownLoadManager;
import com.a520it.googleplay.protocol.HomeProtocol;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:39
 * @desc ${TODD}
 */
public class HomeFragment extends BaseFragment {

    private List<ItemInfoBean> mItemInfoBeans;
    private List<String> mPicture;
    private HomeProtocol mHomeProtocol;
    private HomeAdapter mAdapter;

    /**
     * 真正的在子线程中加载数据, 得到数据
     * 触发加载数据
     *
     * @return
     */
    @Override
    public LoadingPager.LoadedResultState initData() {

        try {
            //处理网络请求, 将返回的数据封装到对象中
            mHomeProtocol = new HomeProtocol();

            HomeInfoBean homeInfoBean = mHomeProtocol.loadData(0);

            //返回状态
            LoadingPager.LoadedResultState state = checkResData(homeInfoBean);


            if (state != LoadingPager.LoadedResultState.SUCCESS) {
                //请求不成功, --->homeInfoBean==null
                return state;
            }


            //校验homeInfoBean.list
            state = checkResData(homeInfoBean.list);
            if (state != LoadingPager.LoadedResultState.SUCCESS) {
                //homeInfoBean.list不成功---->homeInofBean.list.size == 0
                return state;
            }


            //走到这里, 说明数据获取成功
            //保存数据到成员变量
            mItemInfoBeans = homeInfoBean.list;//全部应用信息的集合
            //应用的图片
            mPicture = homeInfoBean.picture;

            //走到这里, 返回状态
            return state;


        } catch (Exception e) {
            e.printStackTrace();
            //出现异常后, 返回一个error
            return LoadingPager.LoadedResultState.ERROR;
        }

    }

    /**
     * 请求成功后显示的界面
     *
     * @return
     */
    @Override
    public View initSuccessView() {
        //创建一个listview
        ListView listView = ListViewFactory.createListView();

        //在设置adapter前, 添加一个轮播图
        // 1. 初始化控件, 绑定数据
        HomePictureHolder homePictureHolder = new HomePictureHolder();

        // 2. listview添加头item
        listView.addHeaderView(homePictureHolder.mHolderView);

        // 3. 传递数据给homePictureHolder, 让其刷新
        homePictureHolder.setDataAndRefreshHolderView(mPicture);

        //绑定适配器
        mAdapter = new HomeAdapter(listView, mItemInfoBeans);
        listView.setAdapter(mAdapter);
        return listView;
    }


    /**
     * 自定义适配器
     */
    class HomeAdapter extends ItemAdapter{
        /**
         * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
         *
         * @param absListView
         * @param datas
         **/
        public HomeAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }
        //数据通过构造方法从外界传入



        /**
         * 返回一个BaseHolder的子类对象
         * 当初始化根控件的时候调用
         *
         * @return 返回一个Holder
         * @param position
         */


        /**
         * 在子线程加载更多数据, 当滑动到底部的时候调用
         * @return
         * @throws Exception
         */
        @Override
        public List<ItemInfoBean> initLoadMoreData() throws Exception {
            SystemClock.sleep(1000);//在子线程中可以睡眠

            HomeInfoBean homeInfoBean = mHomeProtocol.loadData(mItemInfoBeans.size());
            if (homeInfoBean != null) {
                return homeInfoBean.list;
            }
            return null;
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
