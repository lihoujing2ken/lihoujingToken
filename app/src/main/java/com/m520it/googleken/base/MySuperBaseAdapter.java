package com.m520it.googleken.base;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m520it.googleken.factory.ThreadPoolFactory;
import com.m520it.googleken.holder.LoadMoreHolder;
import com.m520it.googleken.utils.UIUtils;

import java.util.List;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  20:20
 * @desc 抽取超类的adapter
 * 如果加载不同的item,就需要复写两个方法 getItemViewType  getViewTypeCount
 */
public abstract class MySuperBaseAdapter<ITEMBEAN> extends MyBaseAdapter {
    public static final int LOADMORE_NORMAL = 0;//加载普通的item
    public static final int LOADMORE_LOADING = 1;//加载更多的item的类型
    public static final int PAGESIZE = 20;//数据的长度
    private LoadMoreHolder mLoadMoreHolder;//LoadMoreHolder 的对象
    private int mStatr;//默认的状态,会随着加载而改变
    private LoadMoreData mLoadMoreData;//加载更多的数据


    //这是去父类中获取数据
    public MySuperBaseAdapter(List datas, final ListView list) {
        super(datas);
        //给item设置点击事件
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把ListView 的头减去再做点击事件
                position = position - list.getHeaderViewsCount();
                //以当前的item来获得当前的状态码,再判断
                if (getItemViewType(position) == LOADMORE_LOADING) {//加载更多
                    //加载失败等于当前的失败的状态码,再调用加载更多的方法
                    if (LoadMoreHolder.LOADMOREHOLDER_ERROR == mStatr) {
                        initLoadMoreData();
                    }
                } else {//否则就打个吐司
                    onItemCilck(parent,view,position,id);
                }
            }
        });
    }


    /**
     * 集合的大小也会改变了,所以要复写这个方法
     */
    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    /**
     * 加载不同的item ,所需要复写的方法
     */
    @Override
    public int getItemViewType(int position) {
        //当前的item为总类型 -1
        if (position == getCount() - 1) {//加载更多的item
            return LOADMORE_LOADING;
        } else {
            //加载普通的item(普通的item也会有不同的类型,所以要抽个方法)
            return getTypeMore(position);
        }
    }

    /**
     * 用于返回普通类型的item,如股还有其他类型的,其子类就可以复写该方法
     *
     * @return
     */
    public int getTypeMore(int position) {
        return LOADMORE_NORMAL;
    }

    /**
     * 加载不同的item ,所需要复写的方法
     * 这个方法是获取当前的item的总类型个数
     * 多加了一种类型,所有返回值要+1
     */
    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount() + 1;
    }

    /**
     * 复写父类的方法,在这实现
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        //找控件和布局
        if (convertView == null) {
            if (getItemViewType(position) == LOADMORE_LOADING) {//加载更多的item
                //这里holder就设置加载更多的数据
                holder = initLoadMoreHolder();
            } else {//加载普通的item
                //这个抽象方法的实现可以给BaseHolder的子类去实现
                holder = SpecialHolder(position);
            }
        } else {
            //获取缓存数据
            holder = (BaseHolder) convertView.getTag();
        }
        //设置数据
        if (getItemViewType(position) == LOADMORE_LOADING) {//加载更多的item
            /**
             * 加载更得的话.有两个需求
             * 1. 把之前的数据放进集合中去
             * 2. 什么时候才会加载更多的数据,这就需要一个开关锁了.
             *     数据都是在关联的时候才会调用的,所以定义个方法用于开启关联数据
             */
            if (hasLoadMore()) {
                initLoadMoreData();
            }

        } else {//加载普通的item
            //调用子类的设置数据方法
            holder.setRefreshDataAndRefreshView(mDatas.get(position));
        }
        //返回的这个对象是返回给 convertView的,所以可以返回 holder.mHomeHolder
        return holder.mHomeHolder;
    }

    /**
     * 给holder找控件和布局
     * 控件和布局那里来呢?创建一个BaseHolder 的子类来实现
     *
     * @return
     */
    private BaseHolder initLoadMoreHolder() {
        //这个的对象为空时,才会创建对象,这样就可以避免多次的创建对象了
        //创建对象的时候,就会调用构造器,也就是可以把控件找到
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        //直接返回这个对象就好了
        return mLoadMoreHolder;
    }

    //用于加载更多的数据
    private void initLoadMoreData() {
        //默认正在加载
        mStatr = LoadMoreHolder.LOADMOREHOLDER_LOADING;
        mLoadMoreHolder.setRefreshDataAndRefreshView(mStatr);//设置默认的状态
        if (mLoadMoreData == null) {
            mLoadMoreData = new LoadMoreData();
            //安全的线程池
            ThreadPoolFactory.createDefaultThreadPool().execute(mLoadMoreData);
        }
    }


    //这个抽象方法给BaseHolder的子类去实现的
    public abstract BaseHolder SpecialHolder(int position);

    //内部类,是用来加载更加数据的
    class LoadMoreData implements Runnable {
        @Override
        public void run() {
            //真正的加载数据过程这里,先声明
            List<ITEMBEAN> loadMoreLists = null;
            //获取默认的状态
            mStatr = LoadMoreHolder.LOADMOREHOLDER_LOADING;
            //创建个方法用于传递数据
            try {
                loadMoreLists = initLoadMore();
                if (loadMoreLists == null) {//数据为空,就是加载失败的状态
                    mStatr = LoadMoreHolder.LOADMOREHOLDER_ERROR;
                } else {//数据不为空
                    if (loadMoreLists.size() == PAGESIZE) {//数据的长度等于PAGESIZE(20)
                        //加载更多
                        mStatr = LoadMoreHolder.LOADMOREHOLDER_LOADING;
                    } else {
                        mStatr = LoadMoreHolder.LOADMOREHOLDER_EMPTY;
                    }
                }
                /**
                 * 走到这里就说明已经获取到了数据,而且数据不为空
                 * 就把加载的数据添加到集合里面中去
                 * 在刷新数据
                 * 把当前的状态码传递到加载更多的数据中去
                 */
                final List<ITEMBEAN> finalLoadMoreLists = loadMoreLists;
                UIUtils.postTaskSafe(new Runnable() {
                    @Override
                    public void run() {

                        if (mDatas == null) {
                            Log.d("TAG", "mDatas=null");

                        }

                        if (finalLoadMoreLists == null) {
                            Log.d("TAG", "finalLoadMoreLists=null");
                        }
                        mDatas.addAll(finalLoadMoreLists);
                        notifyDataSetChanged();
                        mLoadMoreHolder.setRefreshDataAndRefreshView(mStatr);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            //加载数据完了之后把对象置空
            mLoadMoreData = null;
        }
    }


    //这个方法用于给子类传递数据过来
    public List<ITEMBEAN> initLoadMore() {
        return null;
    }

    //用于开启加载更多的数据的开关,默认是false
    public boolean hasLoadMore() {
        return false;
    }

    //给住界面调用的时候打个吐司
    public void onItemCilck(AdapterView<?> parent, View view, int position, long id) {
    }
}
