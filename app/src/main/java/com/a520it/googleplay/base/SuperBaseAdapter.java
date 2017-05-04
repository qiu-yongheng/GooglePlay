package com.a520it.googleplay.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.a520it.googleplay.factory.ThreadPoolProxyFactory;
import com.a520it.googleplay.holder.LoadMoreHolder;
import com.a520it.googleplay.utils.UIUtils;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/20  18:58
 * @desc ${TODD}
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter implements AdapterView.OnItemClickListener {
    //只有两种类型, 注意不要越界
    private static int VIEWTYPE_NOMAL = 0;
    private static int VIEWTYPE_LOADMORE = 1;
    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;
    private static final int PAGERSIZE = 20;
    private int mLoadResultState;
    private final AbsListView mAbsListView;

    /**
     * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
     **/
    public SuperBaseAdapter(AbsListView absListView, List datas) {
        super(datas);
        mAbsListView = absListView;
        //设置点击事件
        mAbsListView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //处理position, 把头文件的索引删除, 修复包名移位
        if (mAbsListView instanceof ListView) {
            //判断listView中有多少个头文件, 删除
            position = position - ((ListView) mAbsListView).getHeaderViewsCount();
        }


        // 1. 判断item类型
        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            //触发加载更多数据
            if (mLoadResultState == LoadMoreHolder.LOADMORE_ERROR) {
                //加载失败, 点击触发加载更多的数据
                triggerLoadMoreData();
            }

        } else {
            //进入详情页, 只有子类知道, 交给子类覆写
            onNomalItemClick(parent, view, position, id);
        }
    }

    /**
     * 进入item的详情页
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onNomalItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 获取控件--->其他方法父类已经实现
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**-----------获取根视图-----------**/

        BaseHolder holder = null;

        if (convertView == null) {
            //分情况返回BaseHolder的子类
            if (getItemViewType(position) == VIEWTYPE_LOADMORE) {//item是普通类型

                //显示加载更多的item--->(是可以确定的: 有三种状态: 正在加载, 加载失败, 没有更多)
                holder = getLoadMoreHolder();

            } else {
                //初始化根视图(给调用的子类实现)
                holder = getSpecialHolder(position);//显示普通的item
            }


        } else {
            holder = (BaseHolder) convertView.getTag();

        }


        /**-----------接收数据, 绑定数据-----------**/
        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            if (hasLoadMore()) {//判断是否有数据, 可以加载更多
                //有数据
                //启动加载更多的方法
                triggerLoadMoreData();
            } else {
                holder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }

        } else {
            //获取BaseAdapter提供的数据
            holder.setDataAndRefreshHolderView(mDataSet.get(position));
        }


        return holder.mHolderView;
    }


    /**
     * 决定是否可以加载更多
     * 子类可以覆写该方法
     *
     * @return
     */
    public boolean hasLoadMore() {
        //默认没有加载更多
        return false;
    }


    /**
     * 触发加载更多的数据
     */
    public void triggerLoadMoreData() {
        if (mLoadMoreTask == null) {
            //设置默认状态为加载中
            int state = LoadMoreHolder.LOADMORE_LOADING;
            //显示加载界面
            mLoadMoreHolder.setDataAndRefreshHolderView(state);


            //在线程池中加载数据
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(mLoadMoreTask);
        }


    }


    class LoadMoreTask implements Runnable {

        @Override
        public void run() {
            //定义刷新UI的初始值(显示加载界面)
            List<ITEMBEANTYPE> loadMoreList = null;
            int state;


            /**-----------1. 加载更多数据, 并根据返回的状态显示界面-----------**/
            try {
                loadMoreList = initLoadMoreData();


                //根据是否有新加载的数据, 获取当前加载状态
                if (loadMoreList == null) {
                    //没有更多数据
                    state = LoadMoreHolder.LOADMORE_NONE;
                } else {
                    if (loadMoreList.size() == PAGERSIZE) {//回来的数据==请求分页条目的长度
                        //还有可能加载更多
                        state = LoadMoreHolder.LOADMORE_LOADING;
                    } else {
                        //回来的数据长度<请求的分页条目长度
                        state = LoadMoreHolder.LOADMORE_NONE;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                //出现异常的时候设置状态为错误
                state = LoadMoreHolder.LOADMORE_ERROR;
            }
            /**------------2. 根据数据, 刷新UI-------------**/

            final List<ITEMBEANTYPE> finalLoadMoreList = loadMoreList;
            mLoadResultState = state;

            //安全刷新UI
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {

                    try {
                        //添加数据到listView中, 刷新
                        mDataSet.addAll(finalLoadMoreList);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mLoadResultState = LoadMoreHolder.LOADMORE_ERROR;
                    }


                    //刷新UI, loadMoreHolder
                    mLoadMoreHolder.setDataAndRefreshHolderView(mLoadResultState);
                }
            });

            //任务完成后, 置空加载更多任务
            mLoadMoreTask = null;
        }

    }


    /**
     * 在子线程加载更多数据, 当滑动到底部的时候调用
     * 读取指定分页的数据
     * 获取当前分页的索引, 解析之后的json数据, 保存到指定对象中
     *
     * @return
     */
    public List<ITEMBEANTYPE> initLoadMoreData() throws Exception {
        return null;
    }


    /**
     * 返回加载更多的item的holder
     *
     * @return
     */
    public LoadMoreHolder getLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }


    /**
     * 获取view类型的数量
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {

        return super.getViewTypeCount() + 1;
    }


    /**
     * 得到指定item的view的类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) {
            //显示最后一个item时, 显示加载更多的item
            return VIEWTYPE_LOADMORE;
        } else {
            //显示普通item
            return getNormalItemViewType(position);
        }
    }

    /**
     * 得到普通条目的viewType类型, 默认是1
     * 子类可以覆写该方法
     *
     * @param position
     * @return
     */
    public int getNormalItemViewType(int position) {
        return VIEWTYPE_NOMAL;//1
    }


    /**
     * 重写getCount()方法
     *
     * @return
     */
    @Override
    public int getCount() {
        //加的1就是加载更多的item
        return super.getCount() + 1;
    }

    /**
     * 初始化根视图(给调用的子类实现)
     * 1. 初始化根视图
     * 2. 初始化子控件
     * 3. 给子控件设置值
     *
     * @param position
     * @return
     */
    public abstract BaseHolder getSpecialHolder(int position);


}
