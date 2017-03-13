package com.m520it.googleken.fragment;

import android.view.View;
import android.widget.ListView;

import com.m520it.googleken.Bean.SubjectBean;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.base.MySuperBaseAdapter;
import com.m520it.googleken.factory.ListViewFactory;
import com.m520it.googleken.holder.SubjectHolser;
import com.m520it.googleken.protocol.SubjectProtocol;

import java.io.IOException;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:08
 * @desc 专题
 */
public class SubjectFragment extends BaseFragment {

    private ListView mListView;
    private List<SubjectBean> mSubjectBeen;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            SubjectProtocol subjectProtocol = new SubjectProtocol();
            mSubjectBeen = subjectProtocol.getHomeInfoBean(0);
            LoadingPager.ResultStateControl resultStateControl = checkResultData(mSubjectBeen);
            if (resultStateControl != LoadingPager.ResultStateControl.SUCCESS) {
                return resultStateControl;
            }
            return LoadingPager.ResultStateControl.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected View initSuccessView() {
        mListView = ListViewFactory.createListView();
        mListView.setAdapter(new SubjectAdapter(mSubjectBeen, mListView));
        return mListView;
    }

    class SubjectAdapter extends MySuperBaseAdapter {

        public SubjectAdapter(List datas, ListView list) {
            super(datas, list);
        }

        @Override
        public BaseHolder SpecialHolder(int position) {
            return new SubjectHolser();
        }
    }
}