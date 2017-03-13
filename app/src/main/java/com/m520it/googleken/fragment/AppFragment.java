package com.m520it.googleken.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.ItemAdapter;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.protocol.AppProtocol;
import com.m520it.googleken.utils.UIUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:07
 * @desc 应用
 */
public class AppFragment extends BaseFragment {

    private List<ItemInfoBean> mAppInfoBean;
    private AppProtocol mAppProtocol;
    private ListView mListview;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            mAppProtocol = new AppProtocol();
            mAppInfoBean = mAppProtocol.getHomeInfoBean(0);
            LoadingPager.ResultStateControl resultData = checkResultData(mAppInfoBean);
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
        mListview = new ListView(UIUtils.getContext());
        mListview.setAdapter(new AppAdapter(mAppInfoBean, mListview));
        return mListview;
    }

    /**
     * 抽出去,共有一个ItemAdapter
     */
    class AppAdapter extends ItemAdapter {

        public AppAdapter(List datas, ListView list) {
            super(datas, list);
        }

        @Override
        public List initLoadMore() {
            try {
                SystemClock.sleep(2 * 1000);
                List<ItemInfoBean> homeInfoBean = mAppProtocol.getHomeInfoBean(mDatas.size());
                return homeInfoBean;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
