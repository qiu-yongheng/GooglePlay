package com.a520it.googleplay.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.protocol.RecommendProtocol;
import com.a520it.googleplay.utils.UIUtils;
import com.a520it.googleplay.views.flyinout.ShakeListener;
import com.a520it.googleplay.views.flyinout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:39
 * @desc ${TODD}
 */
public class RecommendFragment extends BaseFragment {

    private RecommendProtocol mProtocol;
    private List<String> mData;
    private ShakeListener mShakeListener;

    @Override
    public LoadingPager.LoadedResultState initData() {
        mProtocol = new RecommendProtocol();

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
        final StellarMap map = new StellarMap(UIUtils.getContext());

        //拆分屏幕
        map.setRegularity(15, 20);
        final RecommendAdapter adapter = new RecommendAdapter();
        map.setAdapter(adapter);

        //第一页没有展示
        map.setGroup(0, true);

        //添加摇一摇的功能
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //切换到下一页

                //得到当前页
                int currentGroup = map.getCurrentGroup();

                if (currentGroup == adapter.getGroupCount() - 1) {
                    //在最后一页, 回到第一页
                    currentGroup = 0;
                } else {
                    currentGroup++;
                }

                //设置当前页
                map.setGroup(currentGroup, true);
            }
        });
        return map;
    }

    @Override
    public void onResume() {
        if (mShakeListener != null) {
            //恢复监听
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mShakeListener != null) {
            //暂停监听
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommendAdapter implements StellarMap.Adapter {

        public static final int  PAGESIZE = 15;

        @Override
        public int getGroupCount() {
            //计算一共有多少组
            if (mData.size() % PAGESIZE == 0) {
                return mData.size() % PAGESIZE;
            } else {
                return mData.size() % PAGESIZE + 1;
            }
        }

        @Override
        public int getCount(int group) {
            //计算每组有多少个
            if (mData.size() % PAGESIZE == 0) {
                return PAGESIZE;
            } else {
                //如果是最后一组
                if (group == getGroupCount()) {
                    //取余数
                    return mData.size() % PAGESIZE;
                } else {
                    return PAGESIZE;
                }
            }
        }

        @Override
        public View getView(int group, int position, View convertView) {
            TextView tv = new TextView(UIUtils.getContext());

            //计算索引: 组数 * 每组个数 + 当前位置
            int index = group * PAGESIZE + position;

            if (mData.size() <=index) {
                return tv;
            }
            String data = mData.get(index);

            Random random = new Random();

            //设置随机大小
            tv.setTextSize(random.nextInt(4) + 12);
            //设置随机颜色
            int alpah = 255;
            int red = random.nextInt(190) + 30;
            int green = random.nextInt(255);
            int blue = random.nextInt(255);
            int argb = Color.argb(alpah, red, green, blue);
            tv.setTextColor(argb);


            tv.setText(data);
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
