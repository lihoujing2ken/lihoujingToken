package com.m520it.googleken.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m520it.googleken.utils.UIUtils;

import java.util.List;
import java.util.Map;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  9:53
 * @desc Fragment的基类, 常规的抽取
 */
public abstract class BaseFragment extends Fragment {

    public LoadingPager mLoadingPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //另外创建一个类,专门用于来管理Fragment共有的视图
        /*---------定义一个枚举用于返回-----------*//*---------初始化加载成功的容器-----------*/
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            /*---------定义一个枚举用于返回-----------*/
            @Override
            protected ResultStateControl initData() {
                return BaseFragment.this.initData();
            }

            /*---------初始化加载成功的容器-----------*/
            @Override
            protected View initSuccessView() {
                return BaseFragment.this.initSuccessView();
            }
        };
        //需要点到那个引导页再加载,放到activity中去操作
        //loadingPager.trggleDataRefreshState();
        return mLoadingPager;
    }

    /**
     * 加载数据的方法每个fragment都必须有,但是,基类不知道具体的实现过程,所以抽象给子类实现
     *
     * @return
     */
    protected abstract LoadingPager.ResultStateControl initData();

    /**
     * 加载成功的视图,每个fragment加载成功的视图不一样,也只子类自己知道如何显示,所以抽象给子类实现
     *
     * @return
     */
    protected abstract View initSuccessView();

    /**
     * 获取到数据之后,不知值是否已经获取到了数据,所有要做一个判断.在父类中操作
     */
    public LoadingPager.ResultStateControl checkResultData(Object obj) {
        //如果为空,就返回空
        if (obj == null) {
            return LoadingPager.ResultStateControl.EMPTY;
        }
        //如果obj中包含List集合
        if (obj instanceof List) {
            //包含List,如果它的集合的长度为空
            if (((List) obj).size() == 0) {
                return LoadingPager.ResultStateControl.EMPTY;
            }
        }
        //如果obj中包含Map集合
        if (obj instanceof Map) {
            //包含Map,如果它的集合的长度为空
            if (((Map) obj).size() == 0) {
                return LoadingPager.ResultStateControl.EMPTY;
            }
        }
        //以上都不成立的话,就说明数据成功获取到
        return LoadingPager.ResultStateControl.SUCCESS;
    }
}
