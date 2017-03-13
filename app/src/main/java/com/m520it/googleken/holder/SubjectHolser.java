package com.m520it.googleken.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.m520it.googleken.Bean.SubjectBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.UIUtils;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/13  19:09
 * @desc 为 SubjectFragment 界面处理数据的Holder
 */
public class SubjectHolser extends BaseHolder<SubjectBean> {
    private ImageView mItemSubjectIvIcon;
    private TextView mItemSubjectTvTitle;

    @Override
    public void refreshView(SubjectBean data) {
        mItemSubjectTvTitle.setText(data.des);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+data.url).into(mItemSubjectIvIcon);
    }

    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
        mItemSubjectIvIcon = (ImageView) mHomeHolder.findViewById(R.id.item_subject_iv_icon);
        mItemSubjectTvTitle = (TextView) mHomeHolder.findViewById(R.id.item_subject_tv_title);
        return mHomeHolder;
    }
}
