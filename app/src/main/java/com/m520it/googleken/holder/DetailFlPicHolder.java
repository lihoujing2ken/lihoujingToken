package com.m520it.googleken.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.UIUtils;
import com.m520it.googleken.views.MyFrameLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  19:13
 * @desc DetailFlPic 的数据实现类
 */
public class DetailFlPicHolder extends BaseHolder<ItemInfoBean> {
    @Bind(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;


    @Override
    public void refreshView(ItemInfoBean data) {
        //给控件绑定数据
        List<String> screen = data.screen;
        for (int i = 0; i < screen.size(); i++) {
            ImageView iv=new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+screen.get(i)).into(iv);
            MyFrameLayout myFrameLayout=new MyFrameLayout(UIUtils.getContext());
            myFrameLayout.addView(iv);
            //设置款高比
            myFrameLayout.setRelative(150.0f/250);
            //设置当前是已随为确定模式
            myFrameLayout.setCurrentRelative(MyFrameLayout.RELATIVE_WIDTH);
            //屏幕总的宽度
            int widthPixels = UIUtils.getResource().getDisplayMetrics().widthPixels;
            widthPixels= widthPixels - UIUtils.dip2px(12);
            //设置一个精确的宽度
            int wd=widthPixels/3;//手机屏幕宽度减去中间的Margin/3
            int ht= ViewGroup.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(wd,ht);
            if (i!=0){
                params.leftMargin=UIUtils.dip2px(4);
            }
            mAppDetailPicIvContainer.addView(myFrameLayout,params);
        }
    }

    @Override
    public View initHolderView() {
        //找到布局,和布局中的控件
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_pic, null);
        ButterKnife.bind(this, mHomeHolder);
        return mHomeHolder;
    }
}
