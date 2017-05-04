package com.a520it.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.a520it.googleplay.R;
import com.a520it.googleplay.base.BaseHolder;
import com.a520it.googleplay.bean.ItemInfoBean;
import com.a520it.googleplay.conf.Constants;
import com.a520it.googleplay.utils.StringUtils;
import com.a520it.googleplay.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author 邱永恒
 * @time 2016/8/24  16:10
 * @desc ${TODD}
 */
public class DetailInfoHolder extends BaseHolder<ItemInfoBean> {

    @Bind(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @Bind(R.id.app_detail_info_tv_name)
    TextView mAppDetailInfoTvName;
    @Bind(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @Bind(R.id.app_detail_info_tv_downloadnum)
    TextView mAppDetailInfoTvDownloadnum;
    @Bind(R.id.app_detail_info_tv_version)
    TextView mAppDetailInfoTvVersion;
    @Bind(R.id.app_detail_info_tv_time)
    TextView mAppDetailInfoTvTime;
    @Bind(R.id.app_detail_info_tv_size)
    TextView mAppDetailInfoTvSize;

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        String downLoadNum = UIUtils.getString(R.string.detail_downloadnum, data.downloadNum);
        String size = UIUtils.getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
        String updateTime = UIUtils.getString(R.string.detail_time, data.date);
        String version = UIUtils.getString(R.string.detail_version, data.version);


        mAppDetailInfoTvDownloadnum.setText(downLoadNum);//下载数
        mAppDetailInfoTvName.setText(data.name);
        mAppDetailInfoTvSize.setText(size);
        mAppDetailInfoTvTime.setText(updateTime);
        mAppDetailInfoTvVersion.setText(version);

        mAppDetailInfoRbStar.setRating(data.stars);

        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.iconUrl).into(mAppDetailInfoIvIcon);

    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);
        ButterKnife.bind(DetailInfoHolder.this, view);
        return view;
    }
}
