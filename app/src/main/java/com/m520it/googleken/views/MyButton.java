package com.m520it.googleken.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by xmg on 2017/1/15.
 */

public class MyButton extends Button {
    public long  maxSize=0;//当前app的大小
    public long mCurrentSize=0;//当前app下载的大小
    public  boolean isDraw;

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }

    public void setCurrentSize(long currentSize) {
        mCurrentSize = currentSize;
        invalidate();//重绘界面的方法,让已近显示的界面失效,如果不调onDraw不会再次执行
    }

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isDraw){
            //在原有的空间上面画文字
            //canvas.drawText("hahah",30,30,getPaint());
            Drawable drawable=new ColorDrawable(Color.BLUE);
            //画的边界
            int left=0;
            int top=0;
            int right= (int) (mCurrentSize*1.0f/maxSize*getMeasuredWidth());
            int bottom=getBottom();
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(canvas);

        }
        super.onDraw(canvas);

    }
}
