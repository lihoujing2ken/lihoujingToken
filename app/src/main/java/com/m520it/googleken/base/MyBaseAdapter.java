package com.m520it.googleken.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  19:19
 * @desc 抽取BaseAdapter的基类
 */
public class MyBaseAdapter<ITEMBEAN> extends BaseAdapter {
    public List<ITEMBEAN> mDatas;

    public MyBaseAdapter(List<ITEMBEAN> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
