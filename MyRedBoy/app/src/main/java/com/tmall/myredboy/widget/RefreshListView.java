package com.tmall.myredboy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tmall.myredboy.R;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private View        headerView;
    private ImageView   ivArrow;
    private TextView    tvStatus;
    private TextView    tvDate;
    private float       startY;
    private int         headerHeight;
    private View        footerView;
    private ProgressBar footerPb;
    private TextView    foootTvStatus;

    public static final int STATE_PULL_TO_REFRESH    = 0;// 下拉刷新
    public static final int STATE_RELEASE_TO_REFRESH = 1;// 松开刷新
    public static final int STATE_REFRESHING         = 2;// 正在刷新

    // ListView当前状态
    private int mCurrentState = STATE_PULL_TO_REFRESH;
    private int             paddingTop;
    private ProgressBar     pb;
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    private float           moveY;

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
        initAnimation();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RefreshListView(Context context) {
        this(context, null);
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(),
                R.layout.layout_list_refresh_footer, null);

        footerPb = (ProgressBar) footerView.findViewById(R.id.pb);
        foootTvStatus = (TextView) footerView.findViewById(R.id.tv_status);

        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerHeight, 0, 0);

        this.addFooterView(footerView);

        setOnScrollListener(this);

    }

    private void initHeaderView() {
        headerView = View.inflate(getContext(),
                R.layout.layout_list_refresh_header, null);

        ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) headerView.findViewById(R.id.pb);
        tvStatus = (TextView) headerView.findViewById(R.id.tv_status);
        tvDate = (TextView) headerView.findViewById(R.id.tv_date);

        // 初始值设置,最后一次保存的时间
        String lastUpdateTime = PrefUtils.getString(getContext(),
                GlobalConstants.PREF_LAST_UPDATE_TIME, "");

        tvDate.setText(lastUpdateTime);
        // 隐藏头布局,显示效果,不能用此方法
        // headerView.setVisibility(View.GONE);
        // View绘制流程measure-->layout-->draw
        // 如何获取一个控件真实的高度信息？
        // 1、监听视图树
        // 2、手动测量
        headerView.measure(0, 0);
        headerHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerHeight, 0, 0);
        this.addHeaderView(headerView);
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(200);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        downAnimation.setDuration(200);
        downAnimation.setFillAfter(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            // 起点的获取位置(事件传递)
            startY = ev.getY();
        }
        return super.dispatchTouchEvent(ev);
    }

    // 显示头布局<父控件-->子控件>
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // startY = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 正在刷新,头布局高度不需要发生改变,重新获取数据
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }
                moveY = ev.getY();
                float dy = moveY - startY;
                // 向下滑动
                // ListView位于最顶端
                if (dy > 0) {
                    // ListView当前第一个显示的 位置
                    int firstVisiblePosition = getFirstVisiblePosition();
                    if (firstVisiblePosition == 0) {

                        int oldState = mCurrentState;
                        paddingTop = (int) (-headerHeight + dy);

                        if (paddingTop < 0) {
                            mCurrentState = STATE_PULL_TO_REFRESH;
                        } else {
                            mCurrentState = STATE_RELEASE_TO_REFRESH;
                        }

                        // 变化的瞬间调用,否则滑动过程中会一致发生改变
                        // 状态发生改变
                        if (oldState != mCurrentState) {
                            refreshUI();
                        }

                        headerView.setPadding(0, paddingTop, 0, 0);
                        return true;
                    }
                }
                //重置起始坐标,保证头布局从0开始滑出
                startY = moveY;

                break;

            case MotionEvent.ACTION_UP:
                int paddingTop = headerView.getPaddingTop();

                if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    // 影藏头布局
                    headerView.setPadding(0, -headerHeight, 0, 0);
                } else if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshUI();
                    // 刚好显示头布局
                    headerView.setPadding(0, 0, 0, 0);
                    // 正在刷新,需要加载服务器数据
                    notifyRefresh();
                }
                if (paddingTop > -headerHeight) {
                    //取消下拉刷新时,子条目的点击事件,取消选中状态
                    this.setPressed(false);
                    return true;
                }
                if (paddingTop > -headerHeight) {
                    //取消下拉刷新时,子条目的点击事件,取消选中状态
                    this.setPressed(false);
                    return true;
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();// 加载下一页数据
    }

    private OnRefreshListener listener;
    private int               footerHeight;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.listener = listener;
    }

    private void notifyRefresh() {
        if (listener != null) {
            listener.onRefresh();
        }
    }

    private void notifyLoadMore() {
        if (listener != null) {
            listener.onLoadMore();
        }
    }

    private void refreshUI() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvStatus.setText("下拉刷新");
                pb.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(downAnimation);
                ivArrow.setVisibility(View.VISIBLE);
                break;

            case STATE_RELEASE_TO_REFRESH:
                tvStatus.setText("松开刷新");
                pb.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(upAnimation);
                ivArrow.setVisibility(View.VISIBLE);
                break;

            case STATE_REFRESHING:
                tvStatus.setText("正在刷新");
                pb.setVisibility(View.VISIBLE);
                // 之前设置过动画,不可见前必须先清除动画
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                break;

        }
    }

    public void onRefreshComplete(boolean success) {
        System.out.println("onRefreshComplete1");
        // 隐藏头布局
        headerView.setPadding(0, -headerHeight, 0, 0);
        // 状态下拉刷新
        mCurrentState = STATE_PULL_TO_REFRESH;
        // refreshUI();,不需要启动动画
        tvStatus.setText("下拉刷新");
        pb.setVisibility(View.INVISIBLE);
        ivArrow.setVisibility(View.VISIBLE);

        if (success) {
            // 刷新成功之后保存
            setCurrentTime();
            System.out.println("onRefreshComplete1");
        }
    }

    private void setCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date today = new Date();
        String strCurrentTime = sdf.format(today);

        PrefUtils.putString(getContext(),
                GlobalConstants.PREF_LAST_UPDATE_TIME, strCurrentTime);

        // 加载成功之后保存数据
        tvDate.setText(strCurrentTime);

    }

    // 是否正在加载数据
    private boolean isLoadingMore = false;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && !isLoadingMore) {
            int lastVisiblePosition = getLastVisiblePosition();
            // if (lastVisiblePosition == getAdapter().getCount() - 1)
            // 数据的条目数getAdapter().getCount() - 1
            // getCount() listView的item数量,包括头布局和脚布局(getCount() - 1隐藏起来的脚布局)
            if (lastVisiblePosition == getCount() - 1) {
                System.out.println("到底了....");

                isLoadingMore = true;

                // 显示脚布局
                footerView.setPadding(0, 0, 0, 0);
                // 脚布局自动显示
                setSelection(getCount() - 1);

                // 加载下一页数据,添加标记isLoadingMore

                notifyLoadMore();

            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }

    // 通知listView数据已经加载完毕
    public void onMoreDataComplete() {
        // 隐藏脚布局
        footerView.setPadding(0, -footerHeight, 0, 0);

        isLoadingMore = false;
    }

}
