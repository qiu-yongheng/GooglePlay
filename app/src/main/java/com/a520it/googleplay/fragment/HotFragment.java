package com.a520it.googleplay.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a520it.googleplay.base.BaseFragment;
import com.a520it.googleplay.base.LoadingPager;
import com.a520it.googleplay.protocol.HotProtocol;
import com.a520it.googleplay.utils.UIUtils;
import com.a520it.googleplay.views.flyinout.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * @author 邱永恒
 * @time 2016/8/19  16:40
 * @desc ${TODD}
 */
public class HotFragment extends BaseFragment {

    private HotProtocol mProtocol;
    private List<String> mData;

    @Override
    public LoadingPager.LoadedResultState initData() {
        try {
            mProtocol = new HotProtocol();
            mData = mProtocol.loadData(0);
            return checkResData(mData);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResultState.ERROR;
        }
    }

    @Override
    public View initSuccessView() {
        ScrollView sv = new ScrollView(UIUtils.getContext());
        FlowLayout fl = new FlowLayout(UIUtils.getContext());

        for (String data : mData) {
            TextView tv = new TextView(UIUtils.getContext());
            tv.setText(data);


            //设置居中
            tv.setGravity(Gravity.CENTER);
            //设置padding
            int padding = UIUtils.dip2Px(4);
            tv.setPadding(padding, padding, padding, padding);
            tv.setTextColor(Color.WHITE);


            //通过代码的方式创建一个shape图片
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setCornerRadius(8);

            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(190) + 30;
            int green = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);

            normalBg.setColor(argb);

            //创建按下显示的背景图片
            GradientDrawable pressBg = new GradientDrawable();
            pressBg.setCornerRadius(8);
            pressBg.setColor(Color.DKGRAY);




            //通过代码的方式创建selector
            StateListDrawable selectorBg = new StateListDrawable();
            //按下
            selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressBg);
            //默认状态
            selectorBg.addState(new int[]{}, normalBg);


            //设置背景
            tv.setBackgroundDrawable(selectorBg);


            //设置tv可以点击
            tv.setClickable(true);

            //把控件添加到父容器中
            fl.addView(tv);
        }

        sv.addView(fl);
        return sv;
    }
}
