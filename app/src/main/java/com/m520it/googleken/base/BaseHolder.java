package com.m520it.googleken.base;

import android.view.View;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  19:59
 * @desc 抽取Holder的基类
 */
public abstract class BaseHolder<ITEMBEAN> {
    public View mHomeHolder;
    private ITEMBEAN mData;
    //创建构造器的时候就初始化找控件
    public BaseHolder() {
        mHomeHolder = initHolderView();
        mHomeHolder.setTag(this);
    }
    //用调用传递参数
    public void setRefreshDataAndRefreshView(ITEMBEAN data){
        this.mData = data;
        //用于设置数据
        refreshView(mData);
    }
    public abstract void refreshView(ITEMBEAN data);
    public abstract View initHolderView();
}
