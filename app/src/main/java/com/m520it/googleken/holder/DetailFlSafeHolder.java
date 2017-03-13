package com.m520it.googleken.holder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  19:13
 * @desc DetailFlSafe 安全模块的数据实现类
 */
public class DetailFlSafeHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {
    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;
    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;
    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;
    private boolean isOpen = true;//用来控制开关

    //给控件绑定数据
    @Override
    public void refreshView(ItemInfoBean data) {
        //获取出List集合中的数据
        List<ItemInfoBean.SafeInfoBean> safe = data.safe;
        //获取到就遍历
        for (int i = 0; i < safe.size(); i++) {
            /*---------设置图片-----------*/
            //给每个控件设置数据
            ImageView iv = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER + safe.get(i).safeUrl).into(iv);
            mAppDetailSafePicContainer.addView(iv);
            /*----------设置安全描述----------*/
            //描述是由一张图片和一段文字组成
            //获得图片
            LinearLayout linearLayout = new LinearLayout(UIUtils.getContext());
            ImageView imageView = new ImageView(UIUtils.getContext());
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER + safe.get(i).safeDesUrl).into(imageView);
            linearLayout.addView(imageView);
            //获取文字,判断文字是否是安全的,不是安全的就显示红色的字体
            TextView tv = new TextView(UIUtils.getContext());
            //获取安全的状态
            tv.setText(safe.get(i).safeDes);
            if (Integer.valueOf(safe.get(i).safeDesColor) == 0) {
                //说明是安全的
            } else {
                tv.setTextColor(Color.RED);
            }
            linearLayout.addView(tv);
            /*---------再把安全描述放进坑里面-----------*/
            //设置宽高
            int wd = ViewGroup.LayoutParams.WRAP_CONTENT;
            int ht = ViewGroup.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(wd, ht);
            //设置间距
            params.leftMargin = UIUtils.dip2px(8);
            params.topMargin = UIUtils.dip2px(8);
            //把安全描述添加到坑里面去
            mAppDetailSafeDesContainer.addView(linearLayout, params);
        }
        //给当前的mHomeHolder设置点击事件,一点击就把地下的收起来
        mHomeHolder.setOnClickListener(this);
        trggleAniamtion(false);//默认显示状态是不开启的
    }

    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_safe, null);
        ButterKnife.bind(this, mHomeHolder);
        return mHomeHolder;
    }

    @Override
    public void onClick(View v) {
        //这个方法用来控制默认显示的状态
        trggleAniamtion(true);
    }

    private void trggleAniamtion(boolean isAnimtion) {
        //打开--->关闭
        if (isOpen) {
            //激活测量方法,必须要先激活
            mAppDetailSafeDesContainer.measure(0, 0);
            //获得高度
            int height = mAppDetailSafeDesContainer.getMeasuredHeight();
            int startHeight = height;
            int endHeight = 0;//默认显示为0
            if (isAnimtion) {//开始动画
                doAnimation(startHeight, endHeight);
            } else {//改变高度的值
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = endHeight;
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        } else {//下面和上面刚刚好相反
            //关闭-->打开
            mAppDetailSafeDesContainer.measure(0, 0);//激活测量方法
            int height = mAppDetailSafeDesContainer.getMeasuredHeight();
            int startHeight = 0;
            int endHeight = height;
            //做动画
            if (isAnimtion) {
                doAnimation(startHeight, endHeight);
            } else {
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = endHeight;
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        }//完了之后改变状态
        isOpen = !isOpen;
    }
    //属性动画
    private void doAnimation(int startHeight, int endHeight) {
        //做动画
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.start();
        //得到值改变的过程
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //TextView 专属的改变监听器
                Object value = valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height= (int) value;
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });
        //以状态来判断旋转的度数
        if (isOpen){
            ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 180,0);
            rotationAnimator.start();
        }else {
            ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mAppDetailSafeIvArrow, "rotation", 0,180);
            rotationAnimator.start();
        }
    }
}

