package com.m520it.googleken.holder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  19:13
 * @desc DetailFlDes 简介 的数据实现类
 */
public class DetailFlDesHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {
    @Bind(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;//简介
    @Bind(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;//作者
    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;//箭头
    private boolean isOpen = true;//判断状态值
    private int mHeight;
    private ItemInfoBean mDatas;

    @Override
    public void refreshView(ItemInfoBean data) {
        this.mDatas = data;
        //给控件绑定数据
        mAppDetailDesTvDes.setText(data.des);
        mAppDetailDesTvAuthor.setText(data.author);
        //给自身设置点击事件
        mHomeHolder.setOnClickListener(this);
        //渲染字体过后才回调
        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当空间渲染完毕后的回调
            @Override
            public void onGlobalLayout() {
                // mAppDetailDesTvDes.measure(0,0);
                mHeight = mAppDetailDesTvDes.getMeasuredHeight();
                trggleAnimation(false);//动画的默认状态
                mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public View initHolderView() {
        //找到布局,和布局中的控件
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_des, null);
        ButterKnife.bind(this, mHomeHolder);
        return mHomeHolder;
    }

    //点击事件
    @Override
    public void onClick(View v) {
        trggleAnimation(true);
    }

    //动画
    private void trggleAnimation(boolean isAnimation) {
        //以传递的状态判断进入那个方法
        if (isOpen) {
            //获取宽高
            int startHeight = mHeight;
            int endHeight = getTextViewLine(7, mDatas.des);
            if (isAnimation) {
                doAnmaiton(startHeight, endHeight);
            } else {
                //不需要做动画
                mAppDetailDesTvDes.setHeight(endHeight);
            }
        } else {
            int startHeight = getTextViewLine(7, mDatas.des);
            int endHeight = mHeight;
            if (isAnimation) {
                doAnmaiton(startHeight, endHeight);
            } else {
                //不需要做动画
                mAppDetailDesTvDes.setHeight(endHeight);
            }

        }
        isOpen = !isOpen;
    }

    private int getTextViewLine(int i, String des) {
        //默认显示多少行文字
        TextView tv = new TextView(UIUtils.getContext());
        tv.setText(des);//宽度
        tv.setLines(i);//行数
        tv.measure(0, 0);//激活测量
        //返回高度
        return tv.getMeasuredHeight();
    }

    private void doAnmaiton(int startHeight, int endHeight) {
        //启动动画
        ValueAnimator valueAnimator = ObjectAnimator.ofInt(mAppDetailDesTvDes, "height", startHeight, endHeight);
        valueAnimator.start();
        //需求是让scrollView自动滚动到最底部(动画结束后滚动)
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }
            //动画完成后的回调
            @Override
            public void onAnimationEnd(Animator animator) {
                //需求是让scrollView自动滚动到最底部
                ViewParent parent = mAppDetailDesTvDes.getParent();
                while (true) {
                    /**
                     * 找到parent的父类,死循环等于是找到为止
                     */
                    parent = parent.getParent();
                    //找了就判断,判断是有就强转
                    if (parent instanceof ScrollView) {
                        //说明是一个ScrollView,叫ScrollView滑动
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        return;
                    }
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        //以状态来判断判断动画
        if (isOpen) {
            ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 180, 0);
            rotationAnimator.start();
        } else {
            ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(mAppDetailDesIvArrow, "rotation", 0, 180);
            rotationAnimator.start();
        }
    }

}
