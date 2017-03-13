package com.m520it.googleken.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.utils.StringUtils;
import com.m520it.googleken.utils.UIUtils;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  19:13
 * @desc DetailFlInfo 的数据实现类
 */
public class DetailFlInfoHolder extends BaseHolder<ItemInfoBean> {
    private ImageView mAppDetailInfoIvIcon;
    private TextView mAppDetailInfoTvName;
    private RatingBar mAppDetailInfoRbStar;
    private TextView mAppDetailInfoTvDownloadnum;
    private TextView mAppDetailInfoTvVersion;
    private TextView mAppDetailInfoTvTime;
    private TextView mAppDetailInfoTvSize;

    @Override
    public void refreshView(ItemInfoBean data) {
        //给控件绑定数据
        //设置图标
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER+data.iconUrl).into(mAppDetailInfoIvIcon);
        //设置名称
        mAppDetailInfoTvName.setText(data.name);
        //星星
        mAppDetailInfoRbStar.setRating(data.stars);
        String downLoadNumber = UIUtils.getString(R.string.detail_download, data.downloadNum);
        //下载量
        mAppDetailInfoTvDownloadnum.setText(downLoadNumber);
        //版本
        String version = UIUtils.getString(R.string.detail_version, data.version);
        mAppDetailInfoTvVersion.setText(version);
        //时间
        String date = UIUtils.getString(R.string.detail_timer, data.date);
        mAppDetailInfoTvTime.setText(date);
        //当前app的大小
        String size = UIUtils.getString(R.string.detail_size, StringUtils.formatFileSize(data.size));
        mAppDetailInfoTvSize.setText(size);
    }

    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_info, null);
        mAppDetailInfoIvIcon = (ImageView) mHomeHolder.findViewById(R.id.app_detail_info_iv_icon);
        mAppDetailInfoTvName = (TextView) mHomeHolder.findViewById(R.id.app_detail_info_tv_name);
        mAppDetailInfoRbStar = (RatingBar) mHomeHolder.findViewById(R.id.app_detail_info_rb_star);
        mAppDetailInfoTvDownloadnum = (TextView) mHomeHolder.findViewById(R.id.app_detail_info_tv_downloadnum);
        mAppDetailInfoTvVersion = (TextView) mHomeHolder.findViewById(R.id.app_detail_info_tv_version);
        mAppDetailInfoTvTime = (TextView) mHomeHolder.findViewById(R.id.app_detail_info_tv_time);
        mAppDetailInfoTvSize = (TextView) mHomeHolder.findViewById(R.id.app_detail_info_tv_size);

        return mHomeHolder;
    }
}
