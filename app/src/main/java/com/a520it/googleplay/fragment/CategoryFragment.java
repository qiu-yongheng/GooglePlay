package com.a520it.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.base.SuperBaseAdapter;
import com.a520it.googleplay.bean.CategoryInfoBean;
import com.a520it.googleplay.factory.ListViewFactory;
import com.a520it.googleplay.holder.CategoryNormalHolder;
import com.a520it.googleplay.holder.CategoryTitleHolder;
import com.a520it.googleplay.protocol.CategoryProtocol;

import java.util.List;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:40
 * @desc ${TODD}
 */
public class CategoryFragment extends BaseFragment {


    private CategoryProtocol mProtocol;
    private List<CategoryInfoBean> mData;

    @Override
    public LoadingPager.LoadedResultState initData() {
        mProtocol = new CategoryProtocol();

        try {
            mData = mProtocol.loadData(0);
            return checkResData(mData);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultState.ERROR;
        }
    }

    @Override
    public View initSuccessView() {
        ListView listView = ListViewFactory.createListView();

        listView.setAdapter(new CategoryAdapter(listView, mData));
        return listView;
    }

    class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {

        /**
         * item的点击事件是在listView发生的, 所以要传入listView, 传入抽象类, 方便后面groupView使用
         *
         * @param absListView
         * @param datas
         */
        public CategoryAdapter(AbsListView absListView, List datas) {
            super(absListView, datas);
        }

        /**
         * 初始化控件, 设置数据
         * @return
         * @param position
         */
        @Override
        public BaseHolder getSpecialHolder(int position) {
            CategoryInfoBean infoBean = mData.get(position);


            //item的布局不一样, 返回的holder不一样
            if (infoBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }
        }


        /**
         *
         * @param position
         * @return
         */
        @Override
        public int getNormalItemViewType(int position) {
            CategoryInfoBean categoryInfoBean = mData.get(position);

            if (categoryInfoBean.isTitle) {
                return 1;
            } else {
                return 2;
            }
        }

        /**
         * 在父类的基础上, 在添加一种属性
         * @return
         */
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }
    }
}
