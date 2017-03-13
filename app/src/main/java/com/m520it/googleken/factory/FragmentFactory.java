package com.m520it.googleken.factory;

import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.fragment.AppFragment;
import com.m520it.googleken.fragment.CategoryFragment;
import com.m520it.googleken.fragment.GameFragment;
import com.m520it.googleken.fragment.HomeFragment;
import com.m520it.googleken.fragment.HotFragment;
import com.m520it.googleken.fragment.RecommondFragment;
import com.m520it.googleken.fragment.SubjectFragment;

import java.util.HashMap;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:40
 * @desc 工厂类, 专门给Fragment生成
 */
public class FragmentFactory {
    public static final int HOME_FRAGMENT = 0;//首页
    public static final int APP_FRAGMENT = 1;//应用
    public static final int GAME_FRAGMENT = 2;//游戏
    public static final int SUBJECT_FRAGMENT = 3;//专题
    public static final int RECOMMOND_FRAGMENT = 4;//推荐
    public static final int CATEGORY_FRAGMENT = 5;//分类
    public static final int HOT_FRAGMENT = 6;//排行

    /**
     * 创建一个集合,用来缓存Fragment
     */
    public static HashMap<Integer, BaseFragment> fragments = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        //判断,集合中包不包含当前的Fragment,包含就直接返回,不包含后面再添加
        if (fragments.containsKey(position)) {
            return fragments.get(position);
        }
        //定义个空的用来返回
        BaseFragment fragment = null;
        switch (position) {
            case HOME_FRAGMENT://首页
                fragment = new HomeFragment();
                break;
            case APP_FRAGMENT://应用
                fragment = new AppFragment();
                break;
            case GAME_FRAGMENT://游戏
                fragment = new GameFragment();
                break;
            case SUBJECT_FRAGMENT://专题
                fragment = new SubjectFragment();
                break;
            case RECOMMOND_FRAGMENT://推荐
                fragment = new RecommondFragment();
                break;
            case CATEGORY_FRAGMENT://分类
                fragment = new CategoryFragment();
                break;
            case HOT_FRAGMENT://排行
                fragment = new HotFragment();
                break;
        }
        //不包含就直接添加进集合中去
        fragments.put(position, fragment);
        return fragment;
    }
}
