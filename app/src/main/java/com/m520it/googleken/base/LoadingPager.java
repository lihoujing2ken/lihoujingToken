package com.m520it.googleken.base;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.m520it.googleken.R;
import com.m520it.googleken.factory.ThreadPoolFactory;
import com.m520it.googleken.utils.UIUtils;

/**
 * @author lihoujing2ken
 * @time 2017/1/10  9:54
 * @desc Fragment共有的视图
 * initData 的返回值通过枚举来操作
 */
public abstract class LoadingPager extends FrameLayout {
    private View mLoadingView;//正在加载
    private View mEmptyView;//空
    private View mErrorView;//加载失败
    private View mSuccessView;//加载成功

    //定义视图的状态
    public static final int STATE_LOADING = 0;//正在加载
    public static final int STATE_EMPTY = 1;//空的
    public static final int STATE_ERROR = 2;//加载失败
    public static final int STATE_SUCCESS = 3;//加载成功

    public int mCurrnetState = STATE_LOADING; //原始状态
    private LoadTsk mLoadTsk;
    private Button mError_btn_retry;

    public LoadingPager(Context context) {
        super(context);
        //初始化状态
        initCommonView();
    }

    //添加到容器中
    private void initCommonView() {
        //加载中
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        this.addView(mLoadingView);
        //空界面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        this.addView(mEmptyView);
        //加载失败
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        //重新刷新按钮
        mError_btn_retry = (Button) mErrorView.findViewById(R.id.error_btn_retry);
        mError_btn_retry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trggleDataRefreshState();
            }
        });
        this.addView(mErrorView);
        //刷新视图的状态
        refreshViewByState();

    }

    //刷新视图的每一种状态
    private void refreshViewByState() {
        mLoadingView.setVisibility(mCurrnetState == STATE_LOADING ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrnetState == STATE_EMPTY ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrnetState == STATE_ERROR ? View.VISIBLE : View.GONE);
        //加载成功需要判断,再设置值
        if (mCurrnetState == STATE_SUCCESS && mSuccessView == null) {
            mSuccessView = initSuccessView();
            this.addView(mSuccessView);
            mSuccessView.setVisibility(mCurrnetState == STATE_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    //创建个方法给调用
    public void trggleDataRefreshState() {
        //创建个方法在子线程中操作
        /**
         * 判断状态是否再重新加载
         */
        if (mCurrnetState != STATE_SUCCESS && mLoadTsk == null) {
            //设置加载中的状态
            mCurrnetState = STATE_LOADING;
            //条件成立再调用初始化方法
            refreshViewByState();
            mLoadTsk = new LoadTsk();
            //加载数据要在线程中操作,创建了线程池的工厂类
            ThreadPoolFactory.createDefaultThreadPool().execute(mLoadTsk);
            //上面的代码取缔了下面的代码
            //            new Thread(mLoadTsk).start();
        }

    }

    class LoadTsk implements Runnable {
        //创建个工具类,把在子线程中操作的数据转换到主线程
        @Override
        public void run() {
            //加载的状态是子类实现的,所以要抽取出来给子类去实现
            ResultStateControl resultStateControl = initData();
            mCurrnetState = resultStateControl.getState();
            //直接这样操作是不行的,要在主线程中去操作
            UIUtils.postTaskSafe(new Runnable() {
                @Override
                public void run() {
                    refreshViewByState();
                }
            });
            //执行到这里就代表已经初始化完成,就把对象制空
            mLoadTsk = null;
        }
    }

    /**
     * @return
     * @called 当调用trggleDataRefreshState这个方法的时候
     * 加载数据的方法每个fragment都必须有,但是,基类不知道具体的实现过程,所以抽象给子类实现
     */
    protected abstract ResultStateControl initData();

    /**
     * 加载成功的视图,每个fragment加载成功的视图不一样,也只子类自己知道如何显示,所以抽象给子类实现
     *
     * @return
     */
    protected abstract View initSuccessView();

    /**
     * 创建个枚举,用于initData()方法的返回值
     */
    public enum ResultStateControl {
        //1    2   3
        SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR);
        int state;

        public int getState() {
            return state;
        }

        ResultStateControl(int stateEmpty) {
            this.state = stateEmpty;
        }
    }
}
