package com.m520it.googleken.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m520it.googleken.Bean.CategoryInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.UIUtils;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  20:28
 * @desc 用于CategoryFragment 界面的数据转换
 */
public class CategoryInfoHolder extends BaseHolder<CategoryInfoBean> {
    private LinearLayout mItemCategoryItem1;
    private ImageView mItemCategoryIcon1;
    private TextView mItemCategoryName1;
    private LinearLayout mItemCategoryItem2;
    private ImageView mItemCategoryIcon2;
    private TextView mItemCategoryName2;
    private LinearLayout mItemCategoryItem3;
    private ImageView mItemCategoryIcon3;
    private TextView mItemCategoryName3;


    @Override
    public void refreshView(CategoryInfoBean data) {
        //封装
        /*mItemCategoryName1.setText(data.name1);
        mItemCategoryName2.setText(data.name2);
        mItemCategoryName3.setText(data.name3);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+data.url1).into(mItemCategoryIcon1);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+data.url2).into(mItemCategoryIcon2);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+data.url3).into(mItemCategoryIcon3);*/
        //创建一个方法,用于封装起来做点击事件
        initData(data.name1, data.url1, mItemCategoryIcon1, mItemCategoryName1);
        initData(data.name2, data.url2, mItemCategoryIcon2, mItemCategoryName2);
        initData(data.name3, data.url3, mItemCategoryIcon3, mItemCategoryName3);
    }

    private void initData(final String name, String url, ImageView iv, TextView tv) {
        //判断数据是否为空
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)) {
            //不为空就设置数据
            tv.setText(name);
            Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER + url).into(iv);

            //得到它的父亲,以它的父亲来做点击事件
            ViewParent parent = tv.getParent();
            ((ViewGroup) parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), "下手点击的: " + name, Toast.LENGTH_SHORT).show();
                }
            });
            //有数据的就显示
            ((ViewGroup) parent).setVisibility(View.VISIBLE);
        } else {
            //没有数据就隐藏(INVISIBLE和GONE是有区别的)
            ViewParent parent = tv.getParent();
            ((ViewGroup) parent).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_category_info, null);
        mItemCategoryItem1 = (LinearLayout) mHomeHolder.findViewById(R.id.item_category_item_1);
        mItemCategoryIcon1 = (ImageView) mHomeHolder.findViewById(R.id.item_category_icon_1);
        mItemCategoryName1 = (TextView) mHomeHolder.findViewById(R.id.item_category_name_1);
        mItemCategoryItem2 = (LinearLayout) mHomeHolder.findViewById(R.id.item_category_item_2);
        mItemCategoryIcon2 = (ImageView) mHomeHolder.findViewById(R.id.item_category_icon_2);
        mItemCategoryName2 = (TextView) mHomeHolder.findViewById(R.id.item_category_name_2);
        mItemCategoryItem3 = (LinearLayout) mHomeHolder.findViewById(R.id.item_category_item_3);
        mItemCategoryIcon3 = (ImageView) mHomeHolder.findViewById(R.id.item_category_icon_3);
        mItemCategoryName3 = (TextView) mHomeHolder.findViewById(R.id.item_category_name_3);
        return mHomeHolder;
    }
}
