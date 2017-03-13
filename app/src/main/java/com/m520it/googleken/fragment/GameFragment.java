package com.m520it.googleken.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.ItemAdapter;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.protocol.GameProtocol;
import com.m520it.googleken.utils.UIUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:08
 * @desc 游戏
 */
public class GameFragment extends BaseFragment {

    private List<ItemInfoBean> mGameInfoBean;
    private GameProtocol mGameProtocol;
    private ListView mListView;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            mGameProtocol = new GameProtocol();
            mGameInfoBean = mGameProtocol.getHomeInfoBean(0);
            LoadingPager.ResultStateControl resultData = checkResultData(mGameInfoBean);
            if (resultData != LoadingPager.ResultStateControl.SUCCESS) {
                return LoadingPager.ResultStateControl.ERROR;
            }
            return LoadingPager.ResultStateControl.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.ResultStateControl.ERROR;
        }

    }

    @Override
    protected View initSuccessView() {
        mListView = new ListView(UIUtils.getContext());
        mListView.setAdapter(new GameAdapter(mGameInfoBean, mListView));
        return mListView;
    }

    /**
     * 抽出去共用一个ItemAdapter
     */
    class GameAdapter extends ItemAdapter {

        public GameAdapter(List datas, ListView list) {
            super(datas, list);
        }

        @Override
        public List initLoadMore() {
            try {
                SystemClock.sleep(2 * 1000);
                List<ItemInfoBean> homeInfoBean = mGameProtocol.getHomeInfoBean(mDatas.size());
                return homeInfoBean;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
