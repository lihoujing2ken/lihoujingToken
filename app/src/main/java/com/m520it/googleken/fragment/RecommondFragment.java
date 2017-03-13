package com.m520it.googleken.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.m520it.googleken.base.BaseFragment;
import com.m520it.googleken.base.LoadingPager;
import com.m520it.googleken.protocol.RecommondProtocol;
import com.m520it.googleken.utils.UIUtils;
import com.m520it.googleken.views.flyinout.ShakeListener;
import com.m520it.googleken.views.flyinout.StellarMap;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  16:09
 * @desc 推荐
 */
public class RecommondFragment extends BaseFragment {

    private List<String> mRecommondDatas;
    private ShakeListener mShakeListener;
    private RecommondAdapter mRecommondAdapter;

    @Override
    protected LoadingPager.ResultStateControl initData() {
        try {
            RecommondProtocol recommondProtocol = new RecommondProtocol();
            mRecommondDatas = recommondProtocol.getHomeInfoBean(0);
            LoadingPager.ResultStateControl resultData = checkResultData(mRecommondDatas);
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
        //使用框架来做
        final StellarMap stellMap = new StellarMap(UIUtils.getContext());
        //拆分屏幕(固定写法,不管)
        stellMap.setRegularity(15, 20);
        //关联数据
        mRecommondAdapter = new RecommondAdapter();
        stellMap.setAdapter(mRecommondAdapter);
        //默认第一屏不要显示
        stellMap.setGroup(0, true);

        /*---------摇一摇-----------*/
        //设置摇动监听
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //得到当前页
                int currentGroup = stellMap.getCurrentGroup();
                Log.d("TAG","currentGroup="+currentGroup+"mRecommondAdapter.getGroupCount()="+mRecommondAdapter.getGroupCount()   );
                //判断当前页是否相等
                if (currentGroup == mRecommondAdapter.getGroupCount() - 1) {
                    currentGroup = 0;//0代表当前的页面
                } else {
                    currentGroup++;//不是就自增
                }
                //设置
                stellMap.setGroup(currentGroup, true);
            }
        });
        return stellMap;
    }

    //绑定生命周期
    @Override
    public void onResume() {
        //恢复
        if (mShakeListener != null) {
            mShakeListener.resume();
        }
        super.onResume();
    }

    //暂停
    @Override
    public void onPause() {
        if (mShakeListener != null) {
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommondAdapter implements StellarMap.Adapter {
        int PAGESIZE = 15;

        /**
         * 多少组
         *
         * @return
         */
        @Override
        public int getGroupCount() {
            //判断集合的个数和我要显示的个数   46%15
            if (mRecommondDatas.size() % PAGESIZE == 0) {
                //整除就直接返回结果
                return mRecommondDatas.size() / PAGESIZE;
            } else {
                //不整除就要+1 ,因为多的数据要放在另外一页 31%15
                return mRecommondDatas.size() / PAGESIZE + 1;
            }
        }

        /**
         * 每组显示的个数
         *
         * @param group
         * @return
         */
        @Override
        public int getCount(int group) {
            //跟上面一样,整除就直接返回个数
            if (mRecommondDatas.size() % PAGESIZE == 0) {
                Log.d("TAG","mRecommondDatas.size()+"+mRecommondDatas.size()+"+PAGESIZE1="+PAGESIZE);
                return PAGESIZE;//15
            } else {
                //不整除就有两种结果,一种是返回我需要的个数,另外一种是多余出来的个数
                if (group == getGroupCount() - 1) {
                    //另外一种是多余出来的个数
                    Log.d("TAG","mRecommondDatas.size()+"+mRecommondDatas.size()+"+PAGESIZE2="+mRecommondDatas.size() % PAGESIZE);
                    return mRecommondDatas.size() % PAGESIZE;//3
                } else {
                    //一种是返回我需要的个数
                    Log.d("TAG","mRecommondDatas.size()+"+mRecommondDatas.size()+"+PAGESIZE3="+PAGESIZE);
                    return PAGESIZE;//15
                }
            }
        }

        //显示每个item
        @Override
        public View getView(int group, int position, View convertView) {
            //都是TextView
            TextView tv = new TextView(UIUtils.getContext());
            //当前这组的个数的位置
            int positon = group * PAGESIZE + position;
            //获得集合中该组的名称
            String name = mRecommondDatas.get(positon);
            /*---------给item设置属性/颜色/大小/字体-----------*/
            Random random = new Random();
            int size = random.nextInt(5) + 14;
            tv.setTextSize(size);
            int alaph = 255;
            int red = random.nextInt(190) + 30;
            int green = random.nextInt(190) + 30;
            int blue = random.nextInt(30) + 190;
            int color = Color.argb(alaph, red, green, blue);
            tv.setTextColor(color);
            /*---------上面是设置-----------*/
            //添加进去
            tv.setText(name);
            //放回回去
            return tv;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}
