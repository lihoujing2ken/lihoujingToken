package com.m520it.googleken.base;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.m520it.googleken.Bean.ItemInfoBean;
import com.m520it.googleken.activity.DetailActivity;
import com.m520it.googleken.holder.HomeHolder;
import com.m520it.googleken.manager.DownloadManager;
import com.m520it.googleken.utils.UIUtils;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/15  13:41
 * @desc 把相同的 Home/App/Game 界面的Adapter抽取出来做点击事件
 */
public class ItemAdapter extends MySuperBaseAdapter<ItemInfoBean> {
    public ItemAdapter(List datas, ListView list) {
        super(datas, list);
    }

    /**
     * 最后一抽得到的抽象方法,因为这个方法的返回对象是HomeHolder的基类
     *
     * @return 所有这里返回HomeHolder的对象止可
     * @param position
     */
    @Override
    public BaseHolder SpecialHolder(int position) {
        //直接返回这个对象就是实现好的
        HomeHolder holder = new HomeHolder();
        DownloadManager.getInstance().addObserver(holder);
        return holder;
    }

    //传递数据的同时,把开关打开
    @Override
    public boolean hasLoadMore() {
        return true;
    }

    @Override
    public void onItemCilck(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(UIUtils.getContext(),((ItemInfoBean)mDatas.get(position)).packageName,Toast.LENGTH_SHORT).show();
        //可以统一在点击事件这里做跳转,跳转到DetailActivity界面中
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //把需要的包名传递过去
        intent.putExtra("packageName",((ItemInfoBean)mDatas.get(position)).packageName);
        UIUtils.getContext().startActivity(intent);
        super.onItemCilck(parent, view, position, id);
    }
}
