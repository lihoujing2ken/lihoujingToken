package com.m520it.googleken.fragment;

import android.view.View;
import android.widget.ListView;

import com.m520it.googleken.Bean.CategoryInfoBean;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.base.MySuperBaseAdapter;
import com.m520it.googleken.factory.ListViewFactory;
import com.m520it.googleken.holder.CategoryInfoHolder;
import com.m520it.googleken.holder.CategoryTitleHolder;
import com.m520it.googleken.protocol.CategoryProtocol;
import com.m520it.googleken.utils.LogUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:09
 * @desc 分类
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfoBean> mCraegoryInfoBean;
    private CategoryProtocol mCategoryProtocol;
    private ListView mListView;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            mCategoryProtocol = new CategoryProtocol();
            mCraegoryInfoBean = mCategoryProtocol.getHomeInfoBean(0);
            LogUtils.d("ken", "mCraegoryInfoBean=" + mCraegoryInfoBean.toString());
            LoadingPager.ResultStateControl resultData = checkResultData(mCraegoryInfoBean);
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
        mListView = ListViewFactory.createListView();
        mListView.setAdapter(new CategoryAdapter(mCraegoryInfoBean, mListView));
        return mListView;
    }

    class CategoryAdapter extends MySuperBaseAdapter {

        public CategoryAdapter(List datas, ListView list) {
            super(datas, list);
        }

        @Override
        public BaseHolder SpecialHolder(int position) {
            //获取到每个Item
            CategoryInfoBean categoryInfoBean = mCraegoryInfoBean.get(position);
            //获取到boolean状态值,以状态值来判断是否是头
            if (categoryInfoBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryInfoHolder();
            }
        }

        /**
         * 为什么要复写这个方法呢?因为类型增加了,所以要加
         * @return
         */
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount()+1;
        }
        /**
         * 多了一个类型的item,所以要复写父类的方法,用于返回不同的item
         */
        @Override
        public int getTypeMore(int position) {
            //获取头的状态码
            CategoryInfoBean categoryInfoBean = mCraegoryInfoBean.get(position);
            //如果是头,就返回普通的Item
            if(categoryInfoBean.isTitle) {
                return 0;//0 就是普通的Item
                //不是就返回另外的一种类型
            }else{
                return 2;
            }
        }
    }
}
