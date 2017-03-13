package com.m520it.googleken.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.UIUtils;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  13:03
 * @desc 用于实现轮播图
 */
public class PictureHolder extends BaseHolder<List<String>> {
    private ViewPager mItemHomePicturePager;
    private LinearLayout mItemHomePictureContainerIndicator;
    List<String> mDatas;
    //设置数据
    @Override
    public void refreshView(List<String> data) {
        this.mDatas = data;
        //给ViewPager设置数据,数据那里来?Adapter中解析出来
        final MainPictureAdapter mainPictureAdapter = new MainPictureAdapter();
        mItemHomePicturePager.setAdapter(mainPictureAdapter);
        for (int i = 0; i < mDatas.size(); i++) {
            //创建引导其的图片(也就是那个点)
            ImageView iv = new ImageView(UIUtils.getContext());
            //给引导器来个默认值
            if (i == 0) {
                iv.setImageResource(R.drawable.indicator_selected);
            } else {
                iv.setImageResource(R.drawable.indicator_normal);
            }
            //给图片设置参数
            int wh = UIUtils.dip2px(5);
            int ht = UIUtils.dip2px(5);
            //给图片设置参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wh, ht);
            //给每个图片之间设置间距
            params.leftMargin = UIUtils.dip2px(5);
            params.bottomMargin = UIUtils.dip2px(5);
            //把引导器添加进去
            mItemHomePictureContainerIndicator.addView(iv, params);
        }
        //ViewPager的图片和引导器同步
        mItemHomePicturePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动页回调
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //选中页回调
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mDatas.size(); i++) {
                    //获取图片
                    ImageView childAt = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);
                    //给图片来个默认值
                    childAt.setImageResource(R.drawable.indicator_normal);
                    //判断当前值是选中的就设置选中的图片
                    position = position % mDatas.size();
                    if (position == i) {
                        childAt.setImageResource(R.drawable.indicator_selected);
                    }
                }
            }
            //滑动状态改变
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /**
         * 实现无限轮播
         */
        int diff = Integer.MAX_VALUE / 2 % mDatas.size();
        int postion = Integer.MAX_VALUE / 2 - diff;
        mItemHomePicturePager.setCurrentItem(postion);
        //调用自动轮播
        final AutoScroll autoScroll = new AutoScroll();
        autoScroll.start();
        /**
         * 给ViewPager设置触摸监听事件
         */
        mItemHomePicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        autoScroll.stop();
                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        autoScroll.start();
                        break;
                }
                //
                return false;
            }
        });

    }

    //找控件
    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_home_viewpager, null);
        mItemHomePicturePager = (ViewPager) mHomeHolder.findViewById(R.id.item_home_picture_pager);
        mItemHomePictureContainerIndicator = (LinearLayout) mHomeHolder.findViewById(R.id.item_home_picture_container_indicator);
        return mHomeHolder;
    }

    public class MainPictureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mDatas.size();

            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER + mDatas.get(position)).into(iv);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 设置自动循环,用的是一种的的方法.也是使用线程来操作的
     * 已经把自动操作实现了,准备开始调用
     */
    private class AutoScroll implements Runnable {

        //创建个方法用于启动自动轮播
        public void start() {
            UIUtils.getHandler().postDelayed(this, 2 * 1000);
        }

        //停止的方法
        public void stop() {
            UIUtils.getHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            //获得当前显示的界面图片的item
            int currentItem = mItemHomePicturePager.getCurrentItem();
            //自增
            currentItem++;
            //设置当前的Item
            mItemHomePicturePager.setCurrentItem(currentItem);
            //调用Handler方法
            start();
        }
    }
}

