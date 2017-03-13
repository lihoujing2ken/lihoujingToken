package com.m520it.googleken.factory;

import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.m520it.googleken.utils.UIUtils;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  12:02
 * @desc ListView的工厂类, 专门用于生产ListView的
 */
public class ListViewFactory {
    private static ListView mListView;

    public static ListView createListView() {
        mListView = new ListView(UIUtils.getContext());
        //得到快速定位引导条
        mListView.setFastScrollEnabled(true);
        mListView.setCacheColorHint(0);
        mListView.setSelector(new ColorDrawable());
        return mListView;
    }
}