package com.a520it.googleplay.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.CategoryInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/23  21:37
 * @desc ${TODD}
 */
public class CategoryNormalHolder extends BaseHolder<CategoryInfoBean> {


    @Bind(R.id.item_category_icon_1)
    ImageView mItemCategoryIcon1;
    @Bind(R.id.item_category_name_1)
    TextView mItemCategoryName1;
    @Bind(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;
    @Bind(R.id.item_category_icon_2)
    ImageView mItemCategoryIcon2;
    @Bind(R.id.item_category_name_2)
    TextView mItemCategoryName2;
    @Bind(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;
    @Bind(R.id.item_category_icon_3)
    ImageView mItemCategoryIcon3;
    @Bind(R.id.item_category_name_3)
    TextView mItemCategoryName3;
    @Bind(R.id.item_category_item_3)
    LinearLayout mItemCategoryItem3;

    @Override
    public void refreshHolderView(CategoryInfoBean data) {

        initData(data.name1, data.url1, mItemCategoryIcon1, mItemCategoryName1);
        initData(data.name2, data.url2, mItemCategoryIcon2, mItemCategoryName2);
        initData(data.name3, data.url3, mItemCategoryIcon3, mItemCategoryName3);
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
        ButterKnife.bind(this, view);

        return view;
    }

    public void initData(final String name, final String url, ImageView iv, TextView tv) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)) {
            // 1. 给控件设置值
            tv.setText(name);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + url).into(iv);

            // 2. 获取父控件, 设置点击事件
            ViewParent parent = tv.getParent();
            ((ViewGroup)parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //没有数据时, 隐藏item
            ViewParent parent = tv.getParent();
            ((ViewGroup) parent).setVisibility(View.VISIBLE);
        }
    }
}
