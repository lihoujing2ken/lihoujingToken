package com.m520it.googleken.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.astuetz.PagerSlidingTabStripMy;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.factory.FragmentFactory;
import com.m520it.googleken.utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private DrawerLayout mMain_drawerLayout;
    private ActionBarDrawerToggle mBarDrawerToggle;
    private PagerSlidingTabStripMy mMain_tabstrip;
    private ViewPager mMain_viewpager;
    private String[] mMain_trtle;
    private MainPageChangeListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();
        initView();
        initData();
    }

    private void initView() {
        /**
         * 侧滑
         */
        mMain_drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);
        /*---------有一个箭头,要先找到箭头-----------*/
        mBarDrawerToggle = new ActionBarDrawerToggle(this, mMain_drawerLayout, R.string.open, R.string.close);
        /*---------1 要同步toggle 状态-----------*/
        mBarDrawerToggle.syncState();
        /*---------2 设置监听-----------*/
        mMain_drawerLayout.addDrawerListener(mBarDrawerToggle);

        /**
         * 找到控件
         * 引导头
         */
        mMain_tabstrip = (PagerSlidingTabStripMy) findViewById(R.id.main_tabstrip);
        mMain_viewpager = (ViewPager) findViewById(R.id.main_viewpager);

        //需求是:点到那个引导页,再加载数据,所以要给mMain_tabstrip 设置监听数据
        //这个监听类有一个弊端,就是默认的界面不会加载,所以要给他设置一个默认的界面
        //但是,设置默认的界面会报错,为什么呢?
        //因为动态加载控件还没加载完,就要显示界面了,所以才会爆空指针异常
        mListener = new MainPageChangeListener();
        mMain_tabstrip.setOnPageChangeListener(mListener);
        //给ViewPager设置一个默认加载的界面
        //这样直接设置也是有问题的,因为界面还没有渲染完,所以又要监听,渲染完界面之后再设置

        /**
         * 这个监听方法是会阻塞的,所以调用完了之后,要手动关闭
         */
        mMain_viewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //设置默认的界面
                mListener.onPageSelected(0);
                //手动关闭当前的监听方法
                mMain_viewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 侧滑的监听
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBarDrawerToggle.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*---------ActionBar-----------*/
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

    /**
     * 关联数据
     */
    private void initData() {
        /**
         * 获取引导页头部的数据
         */
        mMain_trtle = UIUtils.getStrArr(R.array.main_titles);
        /**
         * 给Adapter传一个Fragment的管理器
         */
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        //给ViewPager设置adapter
        mMain_viewpager.setAdapter(myViewPagerAdapter);
        //把ViewPager设置到mMain_tabstrip中去
        mMain_tabstrip.setViewPager(mMain_viewpager);
    }

    class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        //默认需要
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        //返回每一个Fragment
        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.createFragment(position);
        }

        //返回长度
        @Override
        public int getCount() {
            return mMain_trtle != null ? mMain_trtle.length : 0;
        }

        //返回每个Fragment对应的数据
        @Override
        public CharSequence getPageTitle(int position) {
            return mMain_trtle[position];
        }
    }

    //滚动页的监听
    private class MainPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //滚动到那个的时候再调用初始化的方法
            //在工厂类中调用创建的方法
            BaseFragment fragment = FragmentFactory.createFragment(position);
            //再使用该对象去调用初始化方法
            fragment.mLoadingPager.trggleDataRefreshState();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
