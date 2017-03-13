package com.m520it.googleken.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.protocol.HotProtocol;
import com.m520it.googleken.utils.UIUtils;
import com.m520it.googleken.views.FlowLayout;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:09
 * @desc 排行
 */
public class HotFragment extends BaseFragment {

    private List<String> mHotDatas;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            HotProtocol hotProtocol = new HotProtocol();
            mHotDatas = hotProtocol.getHomeInfoBean(0);
            LoadingPager.ResultStateControl resultData = checkResultData(mHotDatas);
            if (resultData != LoadingPager.ResultStateControl.SUCCESS) {
                return resultData;
            }
            return LoadingPager.ResultStateControl.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.ResultStateControl.ERROR;
        }

    }

    @Override
    protected View initSuccessView() {
        //创建垂直滑动的对象 水平HorizontalScrollView
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        //创建一个流布局的对象
        FlowLayout flowLayout = new FlowLayout(UIUtils.getContext());

        for (int i = 0; i < mHotDatas.size(); i++) {
            TextView tv = new TextView(UIUtils.getContext());
            //获取每一个TextView
            tv.setText(mHotDatas.get(i));
            //设置居中
            tv.setGravity(Gravity.CENTER);
            //设置字体颜色
            tv.setTextColor(Color.WHITE);
            //设置Padding值
            int padding = UIUtils.dip2px(4);
            tv.setPadding(padding, padding, padding, padding);


            /*////设置背景框
            tv.setBackgroundResource(R.drawable.shape_hot_tv);*/
            /*---------以上的方法这这就不适合使用-----------*/
            //同过代码来创建shape的图片
            //创建 GradientDrawable 的对象
            GradientDrawable gd = new GradientDrawable();
            //设置矩形的弧度
            gd.setCornerRadius(8);

            //设置随机色
            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(190) + 30;
            int green = random.nextInt(190) + 30;
            int blue = random.nextInt(190) + 30;
            int argb = Color.argb(alpha, red, green, blue);
            //设置随机色(需要argb就创建)
            gd.setColor(argb);
            /*---------以上设置默认显示的背景效果图-----------*/
            /*---------以下是设置点击的效果图-----------*/
            //选中时候的效果
            GradientDrawable pressDrawable = new GradientDrawable();
            //圆角处的弧度
            pressDrawable.setCornerRadius(8);
            pressDrawable.setColor(Color.DKGRAY);
            /*---------使用代码创建 select 的属性-----------*/
            StateListDrawable stateListDrawable = new StateListDrawable();
            //设置点击的背景
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed},pressDrawable);
            //设置默认的背景
            stateListDrawable.addState(new int[]{},gd);
            /*---------把设置好的 select 添加到tv的背景中-----------*/
            tv.setBackgroundDrawable(stateListDrawable);
            //TextView 默认是没有点击的,设置可以点击
            tv.setClickable(true);

            //把每个TextView添加到流布局中去
            flowLayout.addView(tv);
        }
        scrollView.addView(flowLayout);
        //垂直滑动的布局
        return scrollView;
    }
}
