package com.m520it.googleken.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.m520it.googleken.Bean.CategoryInfoBean;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.utils.UIUtils;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  20:36
 * @desc 用于CategoryFragment 界面 title的数据转换
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {

    private TextView mTv;

    @Override
    public void refreshView(CategoryInfoBean data) {
        mTv.setText(data.title);
        mTv.setTextSize(18);
        mTv.setTextColor(Color.BLACK);
        int padding=UIUtils.dip2px(5);
        //设置边距
        mTv.setPadding(padding,padding,padding,padding);
    }

    @Override
    public View initHolderView() {
        mTv = new TextView(UIUtils.getContext());
        return mTv;
    }
}
