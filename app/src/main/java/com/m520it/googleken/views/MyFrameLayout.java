package com.m520it.googleken.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.m520it.googleken.R;

import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  19:28
 * @desc 自定义FrameLayout 用于图片的切割
 * <p>
 * 在attrs文件中设置自定义属性 格式在最底下
 */
public class MyFrameLayout extends FrameLayout {
    private float mRelative = 444.0f / 183;//宽度/高度的比值
    public static final int RELATIVE_WIDTH = 0;//相对宽度,也就是说宽度是精确值
    public static final int RELATIVE_HEIGHT = 1;//相对高度,也就是说高度是精确值
    public int mCurrentRelative = RELATIVE_WIDTH;

    //方便在代码中设置相对值
    public void setCurrentRelative(int currentRelative) {
        if (currentRelative > 1 || currentRelative < 0) {//代码只能是0 1
            return;
        }
        this.mCurrentRelative = currentRelative;
    }

    //向外暴露方法,用于动态添加
    public void setRelative(float relative) {
        mRelative = relative;
    }

    public MyFrameLayout(Context context) {
        super(context);
    }

    public MyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*---------end 获取XML文件中的属性-----------*/
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyFrameLayout);
        mRelative = a.getFloat(R.styleable.MyFrameLayout_reative, mRelative);
        mCurrentRelative = a.getInt(R.styleable.MyFrameLayout_mode_space, mCurrentRelative);
        a.recycle();
        /*---------ding 获取XML文件中的属性-----------*/
    }
    //如果是修改大小复写onMeasure

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**widthmode
         * 1 不确定(使用不多)
         * 2 精确模式  matchPartent 140dp
         * 3 不精确模式 warpContent
         *
         */
        //获取宽
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);

        //获取高
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        //以宽度为确定值
        if (widthmode == MeasureSpec.EXACTLY && mRelative != 0 && mCurrentRelative == RELATIVE_WIDTH) {
            //求得父亲的宽度
            //求孩子的宽度
            int childWidth = widthSize - getPaddingLeft() - getPaddingRight();
            //求孩子的高度
            int childHeight = (int) (childWidth / mRelative + 0.5f);
            int childWidthMeasureSpec = makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
            //设置会父亲的宽高
            heightSize = childHeight + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(widthSize, heightSize);

            //以高度为确定值
        } else if (heightmode == MeasureSpec.EXACTLY && mRelative != 0 && mCurrentRelative == RELATIVE_HEIGHT) {
            //求孩子的宽度和高度
            int childHeight = heightSize - getPaddingBottom() - getPaddingTop();
            int childWidth = (int) (childHeight * mRelative + 0.5f);
            int childWidthMeasureSpec = makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
            //设置会父亲的宽高
            widthSize = childWidth + getPaddingRight() + getPaddingLeft();
            setMeasuredDimension(widthSize, heightSize);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
    /**
     *
     <resources>
     <declare-styleable name="MyFrameLayout">
     <attr name="reative" format="float"/>
     <attr name="mode_space">
     <enum name="width" value="0"/>
     <enum name="height" value="1"/>
     </attr>
     </declare-styleable>
     </resources>
     */
}
