package com.m520it.googleken.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.m520it.googleken.Bean.HomeInfoBean;
import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.ItemAdapter;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.factory.ListViewFactory;
import com.m520it.googleken.holder.PictureHolder;
import com.m520it.googleken.protocol.HomeProtocol;

import java.io.IOException;
import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:07
 * @desc 首页的Fragment
 */
public class HomeFragment extends BaseFragment {

    public List<ItemInfoBean> mItemInfoBeen;
    private List<String> mPicture;
    private HomeProtocol mHomeProtocol;
    private ListView mListView;
    private HomeListAdapter mHomeadapter;

    //创建成功才会执行的方法
    @Override
    protected View initSuccessView() {
        //使用ListView工厂来生产ListView
        mListView = ListViewFactory.createListView();

        /**
         * 轮播图,就给ListView添加一个头
         * 操作数据的话就放到一个Holder类去实现数据的加载
         */
        PictureHolder pictureHolder = new PictureHolder();
        //设置数据过去
        pictureHolder.setRefreshDataAndRefreshView(mPicture);
        //传递 pictureHolder 中返回的对象
        mListView.addHeaderView(pictureHolder.mHomeHolder);


        //关联数据
        mHomeadapter = new HomeListAdapter(mItemInfoBeen, mListView);
        mListView.setAdapter(mHomeadapter);
        mHomeadapter.notifyDataSetChanged();
        return mListView;
    }

    @Override
    protected LoadingPager.ResultStateControl initData() {
        //数据会有太多一样的,所有要抽代码
        try {
            mHomeProtocol = new HomeProtocol();
            HomeInfoBean homeInfoBean = mHomeProtocol.getHomeInfoBean(0);
            //获取到状态,再判断每种数据返回的类型
            LoadingPager.ResultStateControl resultData = checkResultData(homeInfoBean);
            if (resultData != LoadingPager.ResultStateControl.SUCCESS) {
                return resultData;
            }
            //判断里面的List数据
            resultData = checkResultData(homeInfoBean.list);
            if (resultData != LoadingPager.ResultStateControl.SUCCESS) {
                return resultData;
            }
            //判断里面的Map数据
            resultData = checkResultData(homeInfoBean.picture);
            if (resultData != LoadingPager.ResultStateControl.SUCCESS) {
                return resultData;
            }
            //LogUtils.d("ken", "ken: "+homeInfoBean.toString());
            mPicture = homeInfoBean.picture;
            mItemInfoBeen = homeInfoBean.list;
            //获取到数据就返回一个成功的状态
            return LoadingPager.ResultStateControl.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace();
            //报错页返回一个错误的状态
            return LoadingPager.ResultStateControl.ERROR;
        }
    }

    //最后一抽
    class HomeListAdapter extends ItemAdapter {
        //者传递对象的构造器
        public HomeListAdapter(List datas, ListView lists) {
            super(datas, lists);
        }
        //传递数据
        @Override
        public List<ItemInfoBean> initLoadMore() {
            SystemClock.sleep(2 * 1000);
            try {
                //每次加载就是集合大小的数据
                HomeInfoBean homeInfoBean = mHomeProtocol.getHomeInfoBean(mDatas.size());
                //返回需要的数据
                return homeInfoBean.list;
            } catch (IOException e) {
                e.printStackTrace();
                //报错就返回空
                return null;
            }
        }
    }

}
