package com.m520it.googleken.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  14:52
 * @desc 自定义ViewPager
 * ViewPager嵌套ViewPager在不同版本的API中获得的焦点都不一样
 * 自定义计算坐标,改变状态,复写方法
 */
public class MyViewPager extends ViewPager {

    private float mStartX;
    private float mStartY;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                //获取按下的坐标
                mStartX = ev.getRawX();
                mStartY = ev.getRawY();
                break;
            //抬起
            case MotionEvent.ACTION_MOVE:
                //获取抬起的坐标
                float rawX = ev.getRawX();
                float rawY = ev.getRawY();
                //求出相差值
                float dx = rawX - mStartX;
                float dy = rawY - mStartY;
                //以相差值来判断滑动的方向
                if (Math.abs(dx) > Math.abs(dy)) {
                    //左右滑动
                    //请求父ViewPager不拦截子ViewPager的滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    //上下滑动
                    //请求父ViewPager拦截子ViewPager的滑动事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(ev);
    }
}
