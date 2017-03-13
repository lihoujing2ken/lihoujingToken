package com.m520it.googleken.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.holder.DetailFlBottomHolder;
import com.m520it.googleken.holder.DetailFlDesHolder;
import com.m520it.googleken.holder.DetailFlInfoHolder;
import com.m520it.googleken.holder.DetailFlPicHolder;
import com.m520it.googleken.holder.DetailFlSafeHolder;
import com.m520it.googleken.manager.DownloadInfo;
import com.m520it.googleken.manager.DownloadManager;
import com.m520it.googleken.protocol.DetailProtocol;
import com.m520it.googleken.utils.LogUtils;
import com.m520it.googleken.utils.UIUtils;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private String mPackageName;
    private ItemInfoBean mDetailDatas;
    private FrameLayout mDetailFlBottom;
    private FrameLayout mDetailFlInfo;
    private FrameLayout mDetailFlSafe;
    private FrameLayout mDetailFlPic;
    private FrameLayout mDetailFlDes;
    private DetailFlBottomHolder mBottom;
    private DownloadInfo mDownloadInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_detail);
        //获取传递过来的数据
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra("packageName");//获取到包名
        //ActionBar
        //        LogUtils.d("TAG", "mPackageName=====" + mPackageName);
        initActionBar();
        //初始化控件
        initView();
    }

    private void initView() {
        //在此找布局和获取控件
        LoadingPager loadingPager = new LoadingPager(UIUtils.getContext()) {
            //获取数据协议
            @Override
            protected ResultStateControl initData() {
                //抽个方法来实现吧
                return DetailActivity.this.initData();
            }

            //找控件
            @Override
            protected View initSuccessView() {
                /**
                 * 找到每个架子,然后往每个架子里面添加东西
                 */
                View view = View.inflate(UIUtils.getContext(), R.layout.item_app_detail, null);
                /*---------mDetailFlBottom 下载-----------*/
                mDetailFlBottom = (FrameLayout) view.findViewById(R.id.detail_fl_bottom);

                mBottom = new DetailFlBottomHolder();
                DownloadManager.getInstance().addObserver(mBottom);
                mBottom.setRefreshDataAndRefreshView(mDetailDatas);
                mDetailFlBottom.addView(mBottom.mHomeHolder);




                /*---------mDetailFlInfo 应用详情-----------*/
                mDetailFlInfo = (FrameLayout) view.findViewById(R.id.detail_fl_info);
                DetailFlInfoHolder Info = new DetailFlInfoHolder();
                Info.setRefreshDataAndRefreshView(mDetailDatas);
                mDetailFlInfo.addView(Info.mHomeHolder);
                /*----------DetailFlSafe 安全模块 ----------*/
                mDetailFlSafe = (FrameLayout) view.findViewById(R.id.detail_fl_safe);
                DetailFlSafeHolder safe = new DetailFlSafeHolder();
                safe.setRefreshDataAndRefreshView(mDetailDatas);
                mDetailFlSafe.addView(safe.mHomeHolder);
                /*---------DetailFlPic 图片-----------*/
                mDetailFlPic = (FrameLayout) view.findViewById(R.id.detail_fl_pic);
                DetailFlPicHolder pic = new DetailFlPicHolder();
                pic.setRefreshDataAndRefreshView(mDetailDatas);
                mDetailFlPic.addView(pic.mHomeHolder);
                /*---------DetailFlDes 简介-----------*/
                mDetailFlDes = (FrameLayout) view.findViewById(R.id.detail_fl_des);
                DetailFlDesHolder des = new DetailFlDesHolder();
                des.setRefreshDataAndRefreshView(mDetailDatas);
                mDetailFlDes.addView(des.mHomeHolder);
                return view;
            }
        };
        // 显示成功页面
        setContentView(loadingPager);
        loadingPager.trggleDataRefreshState();
    }

    //获取网络的数据,在DetailProtocol中获取
    private LoadingPager.ResultStateControl initData() {
        /**
         * 是以包名来获取数据的,所以要把包名的信息传过去
         */
        try {
            DetailProtocol detailProtocol = new DetailProtocol(mPackageName);
            mDetailDatas = detailProtocol.getHomeInfoBean(0);
            LogUtils.d("TAG", "mDetailDatas :" + mDetailDatas.toString());
            if (mDetailDatas == null) {
                return LoadingPager.ResultStateControl.ERROR;
            }
            //LogUtils.d("TAG","mDetailDatas :"+mDetailDatas.toString());
            return LoadingPager.ResultStateControl.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return LoadingPager.ResultStateControl.ERROR;
        }

    }

    //ActionBar
    private void initActionBar() {
 /* mActionBar.setDisplayShowTitleEnabled(true);// 设置菜单 标题是否可见
        mActionBar.setDisplayShowHomeEnabled(false);// 设置应用图标是否
        mActionBar.setDisplayUseLogoEnabled(false);// 设置是否显示Logo优先*/
        // 获取ActionBar
        mActionBar = getSupportActionBar();
        mActionBar.setTitle("GooglePlayer");// 设置主title部分
        //mActionBar.setSubtitle("SubTitle");// 设置子title部分
        mActionBar.setIcon(R.mipmap.ic_launcher);// 设置应用图标
        mActionBar.setDisplayHomeAsUpEnabled(true);// 设置back按钮是否可见
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //界面可见的时候添加会观察者
    @Override
    protected void onResume() {
        super.onResume();
        if (mBottom != null) {
            DownloadManager.getInstance().addObserver(mBottom);
            mDownloadInfo = DownloadManager.getInstance().getDownloadInfo(mDetailDatas);
            DownloadManager.getInstance().notifyObservers(mDownloadInfo);
        }

    }
    //界面不可见的时候移除观察者

    @Override
    protected void onPause() {
        super.onPause();
        if (mBottom != null) {
            DownloadManager.getInstance().deleteObserver(mBottom);
        }

    }
}
