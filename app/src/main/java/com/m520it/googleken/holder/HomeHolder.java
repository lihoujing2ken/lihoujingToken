package com.m520it.googleken.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.config.Constants;
import com.m520it.googleken.manager.DownloadInfo;
import com.m520it.googleken.manager.DownloadManager;
import com.m520it.googleken.utils.CommonUtils;
import com.m520it.googleken.utils.StringUtils;
import com.m520it.googleken.utils.UIUtils;
import com.m520it.googleken.views.ProgressView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  19:39
 * @desc 给HomeFragment界面初始化item
 */
public class HomeHolder extends BaseHolder<ItemInfoBean> implements DownloadManager.MessageObserver {
    @Bind(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;
    @Bind(R.id.item_appinfo_tv_title)
    TextView mItemAppinfoTvTitle;
    @Bind(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;
    @Bind(R.id.item_appinfo_tv_size)
    TextView mItemAppinfoTvSize;
    @Bind(R.id.item_appinfo_tv_des)
    TextView mItemAppinfoTvDes;
    @Bind(R.id.item_appinfo_progressview)
    ProgressView mItemAppinfoProgressview;
    private ItemInfoBean mData;

    /**
     * 找控件
     */
    @Override
    public View initHolderView() {
        //使用工具找的ID
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_info, null);

        ButterKnife.bind(this, mHomeHolder);
        //返回的这个对象就是缓存的对象
        return mHomeHolder;
    }

    /**
     * 设置数据
     */
    @Override
    public void refreshView(ItemInfoBean data) {
        mItemAppinfoTvTitle.setText(data.name);
        //设置Rating是使用这个方法
        mItemAppinfoRbStars.setRating(data.stars);
        //Formatter.formatFileSize()
        //使用工具类把 long类型转成String类型
        String fileSize = StringUtils.formatFileSize(data.size);
        mItemAppinfoTvSize.setText(fileSize);
        mItemAppinfoTvDes.setText(data.des);
        //图片加载使用了 picasso
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.LOADIMAGER + data.iconUrl).into(mItemAppinfoIvIcon);
        this.mData = data;

        /**
         ratingbar
         -->展示功能-->不给你评分
         -->评分功能-->1.5
         */

          /*--------------- 2.根据不同的状态给用户提示(修改下载按钮的ui) --------------*/

        //DownloadIno(curState)
        DownloadInfo downLoadInfo = DownloadManager.getInstance().getDownloadInfo(mData);
        refreshProgressViewUi(downLoadInfo);
    }

    /**
     * 根据downLoadInfo里面的状态刷新下载按钮的对应的ui
     *
     * @param downLoadInfo
     */
    private void refreshProgressViewUi(DownloadInfo downLoadInfo) {
        int curState = downLoadInfo.mCurrentState;
        /**

         状态(编程记录)  	|  给用户的提示(ui展现)
         未下载			|下载
         下载中			|显示进度条
         暂停下载		|继续下载
         等待下载		|等待中...
         下载失败 		|重试
         下载完成 		|安装
         已安装 			|打开
         */
        switch (curState) {
            case DownloadManager.STATE_UNDOWNLOAD://未下载
                mItemAppinfoProgressview.setNote("下载");
                mItemAppinfoProgressview.setIcon(R.drawable.ic_download);
                break;
            case DownloadManager.STATE_DOWNLOADING://下载中
                mItemAppinfoProgressview.setIcon(R.drawable.ic_pause);
                int progress = (int) (downLoadInfo.mCurrentProcess * 1.0 / downLoadInfo.maxSize * 100 + .5f);
                mItemAppinfoProgressview.setIsProgressEnable(true);
                mItemAppinfoProgressview.setNote(progress + "%");//50  %

                mItemAppinfoProgressview.setMax(downLoadInfo.maxSize);
                mItemAppinfoProgressview.setProgress(downLoadInfo.mCurrentProcess);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mItemAppinfoProgressview.setIcon(R.drawable.ic_resume);
                mItemAppinfoProgressview.setNote("继续下载");
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
                mItemAppinfoProgressview.setIcon(R.drawable.ic_pause);
                mItemAppinfoProgressview.setNote("等待中...");

                break;
            case DownloadManager.STATE_DOWNLOADFAILED://下载失败
                mItemAppinfoProgressview.setIcon(R.drawable.ic_redownload);
                mItemAppinfoProgressview.setNote("重试");

                break;
            case DownloadManager.STATE_DOWNLOADED://下载完成
                mItemAppinfoProgressview.setIcon(R.drawable.ic_install);
                mItemAppinfoProgressview.setIsProgressEnable(false);
                mItemAppinfoProgressview.setNote("安装");

                break;
            case DownloadManager.STATE_INSTALLED://已安装
                mItemAppinfoProgressview.setIcon(R.drawable.ic_install);
                mItemAppinfoProgressview.setNote("打开");

                break;

            default:
                break;
        }
    }

    @OnClick(R.id.item_appinfo_progressview)
    public void clickProgressView(View v) {

        /*--------------- 3.根据不同的状态触发不同的操作 ---------------*/
        //DownloadInfo(curState)
        DownloadInfo downLoadInfo = DownloadManager.getInstance().getDownloadInfo(mData);
        int curState = downLoadInfo.mCurrentState;
        /**

         状态(编程记录  | 用户行为(触发操作)
         ----------------| -----------------
         未下载			| 去下载
         下载中			| 暂停下载
         暂停下载		| 断点继续下载
         等待下载		| 取消下载
         下载失败 		| 重试下载
         下载完成 		| 安装应用
         已安装 			| 打开应用

         */
        switch (curState) {
            case DownloadManager.STATE_UNDOWNLOAD://未下载
                doDownLoad(downLoadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADING://下载中
                pauseDownLoad(downLoadInfo);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
                doDownLoad(downLoadInfo);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
                cancelDownLoad(downLoadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED://下载失败
                doDownLoad(downLoadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADED://下载完成
                installApk(downLoadInfo);
                break;
            case DownloadManager.STATE_INSTALLED://已安装
                openApk(downLoadInfo);
                break;

            default:
                break;
        }
    }

    /**
     * 打开apk
     *
     * @param downLoadInfo
     */
    private void openApk(DownloadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 安装apk
     *
     * @param downLoadInfo
     */
    private void installApk(DownloadInfo downLoadInfo) {
        File apkFile = new File(downLoadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 开始下载,继续下载,重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownloadInfo downLoadInfo) {
        //下载url
        //apk保存到哪里
       /* DownLoadInfo info = new DownLoadInfo();
        info.max = mItemInfoBean.size;
        info.name = mItemInfoBean.downloadUrl;

        String dir = FileUtils.getDir("apk");//sdcard/Android/data/包目录/apk
        String fileName = mItemInfoBean.packageName + ".apk";
        File saveFile = new File(dir, fileName);

        info.savePath = saveFile.getAbsolutePath();
        info.packageName = mItemInfoBean.packageName;*/

        DownloadManager.getInstance().download(downLoadInfo);
    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void pauseDownLoad(DownloadInfo downLoadInfo) {
        DownloadManager.getInstance().AppPauseDownload(downLoadInfo);
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void cancelDownLoad(DownloadInfo downLoadInfo) {
        DownloadManager.getInstance().AppCancelDownload(downLoadInfo);
    }


    @Override
    public void messageChanger(final DownloadInfo downLoadInfo) {
        //过滤传递过来的downloadInfo信息
        if (!downLoadInfo.packageName.equals(mData.packageName)) {
            return;
        }
        //        PrintDownLoadInfo.printDownLoadInfo(downLoadInfo);
        UIUtils.postTaskSafe(new Runnable() {
            @Override
            public void run() {
                //刷新ui
                refreshProgressViewUi(downLoadInfo);
            }
        });
    }
}

