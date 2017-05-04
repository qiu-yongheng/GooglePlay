package com.a520it.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.base.SuperBaseAdapter;
import com.a520it.googleplay.bean.SubjectInfoBean;
import com.a520it.googleplay.factory.ListViewFactory;
import com.a520it.googleplay.holder.SubjectHolder;
import com.a520it.googleplay.protocol.SubjectProtocol;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:39
 * @desc ${TODD}
 */
public class SubjectFragment extends BaseFragment {


    private SubjectProtocol mProtocol;
    private List<SubjectInfoBean> mData;

    @Override
    public LoadingPager.LoadedResultState initData() {
        mProtocol = new SubjectProtocol();

        try {
            //解析json数据, 封装到对象中
            mData = mProtocol.loadData(0);
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

        // 2. 绑定adapter
        listView.setAdapter(new SubjectAdapter(listView, mData));
        return listView;
    }

    class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {

        /**
         * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
         *
         * @param absListView
         * @param datas
         */
        public SubjectAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         * 初始化视图, 设置数据
         * @return
         * @param position
         */
        @Override
        public BaseHolder getSpecialHolder(int position) {

            return new SubjectHolder();
        }

        @Override
        public boolean hasLoadMore() {
            return true;
        }

        /**
         * 加载更多数据
         * @return
         * @throws Exception
         */
        @Override
        public List<SubjectInfoBean> initLoadMoreData() throws Exception {
            List<SubjectInfoBean> subjectInfoBeen = mProtocol.loadData(mData.size());
            return subjectInfoBeen;
        }
    }
}
