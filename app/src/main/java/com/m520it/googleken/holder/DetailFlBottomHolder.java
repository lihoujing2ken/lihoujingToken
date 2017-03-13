package com.m520it.googleken.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.R;
import com.m520it.googleken.base.BaseHolder;
import com.m520it.googleken.manager.DownloadInfo;
import com.m520it.googleken.manager.DownloadManager;
import com.m520it.googleken.utils.CommonUtils;
import com.m520it.googleken.utils.UIUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  19:05
 * @desc DetailFlBottomHolder 下载应用的具体需求
 */
public class DetailFlBottomHolder extends BaseHolder<ItemInfoBean> implements DownloadManager.MessageObserver{

    @Bind(R.id.app_detail_download_btn_favo)
    Button mAppDetailDownloadBtnFavo;
    @Bind(R.id.app_detail_download_btn_share)
    Button mAppDetailDownloadBtnShare;
    @Bind(R.id.app_detail_download_btn_download)
    com.m520it.googleken.views.MyButton mAppDetailDownloadBtnDownload;//自定义的控件
    private TextView mTextView;
    private ItemInfoBean mItemInfoBean;
    private DownloadInfo mDownloadInfo;

    //找控件
    @Override
    public View initHolderView() {
        mHomeHolder = View.inflate(UIUtils.getContext(), R.layout.item_app_detail_bottom, null);
        //使用框架
        ButterKnife.bind(this, mHomeHolder);
        return mHomeHolder;
    }
    //控件绑定数据
    @Override
    public void refreshView(ItemInfoBean data) {
        //给控件绑定数据
        this.mItemInfoBean=data;
        //得到状态
        /*---------得到状态,然后根据不同的状态给用户不同的界面提示(需要得到DownloadInfo)--------*/
        mDownloadInfo = DownloadManager.getInstance().getDownloadInfo(mItemInfoBean);
        mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        refreshView(mDownloadInfo);
    }
    //判断背包中的状态,再做具体的操作
    private void refreshView(DownloadInfo DownloadInfo) {
        int currentState = DownloadInfo.mCurrentState;//得到存放的状态
        switch (currentState){
            case DownloadManager.STATE_UNDOWNLOAD://未下载
                mAppDetailDownloadBtnDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
                mAppDetailDownloadBtnDownload.setText("等待中....");
                break;
            case DownloadManager.STATE_DOWNLOADING ://下载中
                mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                //打开可以绘制的开关
                mAppDetailDownloadBtnDownload.setDraw(true);
                //当前的下载process
                mAppDetailDownloadBtnDownload.setCurrentSize(mDownloadInfo.mCurrentProcess);
                //当前apk的总的大小
                mAppDetailDownloadBtnDownload.setMaxSize(mDownloadInfo.maxSize);
                int process= (int) (mDownloadInfo.mCurrentProcess*1.0f/mDownloadInfo.maxSize*100+0.5f);
                mAppDetailDownloadBtnDownload.setText(process+"%");
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
                mAppDetailDownloadBtnDownload.setText("继续下载");
                break;
            case DownloadManager.STATE_DOWNLOADFAILED://下载失败
                mAppDetailDownloadBtnDownload.setText("重试");
                break;
            case DownloadManager.STATE_DOWNLOADED://下载完成
                mAppDetailDownloadBtnDownload.setDraw(false);
                mAppDetailDownloadBtnDownload.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
                mAppDetailDownloadBtnDownload.setText("安装");
                break;
            case DownloadManager.STATE_INSTALLED://安装
                mAppDetailDownloadBtnDownload.setText("打开");
                break;
        }
    }

   //ButterKnife 控件的点击事件
    @OnClick(R.id.app_detail_download_btn_download)
    public void downLoad(){
        // 点击就开始下载
        /*---------得到状态,下一步的动作(需要得到DownloadInfo)--------*/
        switch (mDownloadInfo.mCurrentState){
            case DownloadManager.STATE_UNDOWNLOAD://未下载
                AppDownload(mDownloadInfo);
                break;
            case DownloadManager.STATE_WAITINGDOWNLOAD://等待下载
                AppCancelDownload(mDownloadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADING ://下载中
                AppPauseDownload(mDownloadInfo);
                break;
            case DownloadManager.STATE_PAUSEDOWNLOAD://暂停下载
                AppDownload(mDownloadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADFAILED://下载失败
                AppDownload(mDownloadInfo);
                break;
            case DownloadManager.STATE_DOWNLOADED://下载完成
                AppInstall(mDownloadInfo);
                break;
            case DownloadManager.STATE_INSTALLED://安装
                AppOpen(mDownloadInfo);
                break;
        }
    }
    //打开app
    private void AppOpen(DownloadInfo downloadInfo) {
        CommonUtils.openApp(UIUtils.getContext(),downloadInfo.packageName);
    }
    //App安装
    private void AppInstall(DownloadInfo downloadInfo) {
        File file=new File(downloadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(),file);
    }
    //app暂停下载
    private void AppPauseDownload(DownloadInfo downloadInfo) {
        DownloadManager.getInstance().AppPauseDownload(downloadInfo);
    }
    //取消app下载的方法
    private void AppCancelDownload(DownloadInfo downloadInfo) {
        DownloadManager.getInstance().AppCancelDownload(downloadInfo);
    }
    //专门用来下载的方法
    private void AppDownload(DownloadInfo downloadInfo) {
        DownloadManager.getInstance().download(downloadInfo);
    }
    /**###########观察者###########**/
    //这是自定义的观察者方法
    @Override
    public void messageChanger(final DownloadInfo downloadInfo) {
        //判断包名不相等就直接结束
        if (!downloadInfo.packageName.equals(mItemInfoBean.packageName) ){
            return;
        }
        UIUtils.postTaskSafe(new Runnable() {
            @Override
            public void run() {
                //刷新UI
                refreshView(downloadInfo);
            }
        });
    }
}
