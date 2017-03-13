package com.m520it.googleken.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.utils.UIUtils;

/**
 * @author lihoujing2ken
 * @time 2017/1/12  17:35
 * @desc 加载更多数据的Holder
 * 这里的类型为什么是Integer类型呢?因为需要个状态码
 */
public class LoadMoreHolder extends BaseHolder<Integer> {
    public static final int LOADMOREHOLDER_LOADING = 0;//正在加载更多
    public static final int LOADMOREHOLDER_ERROR = 1;//加载更多失败
    public static final int LOADMOREHOLDER_EMPTY = 2;//空的状态
    private LinearLayout mItemLoadmoreContainerLoading;//加载中
    private LinearLayout mItemLoadmoreContainerRetry;//加载失败

    /**
     * 这个是返回状态码
     * 这个方法就是控制状态的显示的
     *
     * @param data
     */
    @Override
    public void refreshView(Integer data) {
        //默认都隐藏的
        mItemLoadmoreContainerLoading.setVisibility(View.GONE);
        mItemLoadmoreContainerRetry.setVisibility(View.GONE);
        //设置状态
        switch (data) {
            //等于 0 ,加载的状态就显示,失败的就隐藏
            case LOADMOREHOLDER_LOADING:
                mItemLoadmoreContainerLoading.setVisibility(View.VISIBLE);
                mItemLoadmoreContainerRetry.setVisibility(View.GONE);
                break;
            //等于1 ,加载的状态就隐藏,失败的就显示
            case LOADMOREHOLDER_ERROR:
                mItemLoadmoreContainerLoading.setVisibility(View.GONE);
                mItemLoadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 这个方法是初始化对象的时候就会调用的
     * 所以在这找控件和布局
     *
     * @return
     */
    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);
        mItemLoadmoreContainerLoading = (LinearLayout) mHomeHolder.findViewById(R.id.item_loadmore_container_loading);
        mItemLoadmoreContainerRetry = (LinearLayout) mHomeHolder.findViewById(R.id.item_loadmore_container_retry);
        return mHomeHolder;
    }
}
