package com.tmall.myredboy.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

/**
 * 支持上拉加载
 * 底部没有进度条
 */

public abstract class BaseLoadMoreAdapter<T> extends BaseAdapter<T> {

    private Context            mContext;
    private RecyclerView       mRecyclerView;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading = false;


    public BaseLoadMoreAdapter(Context mContext, RecyclerView recyclerView, List mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.mRecyclerView = recyclerView;
        this.mContext = mContext;
        init();
    }

    private void init() {
        //对于addOnScrollListener方法,维护一个listener集合,可用添加多个监听,执行时会依次遍历
        mRecyclerView.clearOnScrollListeners();
        //mRecyclerView添加滑动事件监听
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!isLoading && dy > 0 && lastVisibleItemPosition >= totalItemCount - 5) {
                    //此时是刷新状态
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
                if (dy < 30 && lastCompletelyVisibleItemPosition == totalItemCount - 1) {
                    ToastUtils.show(mContext, "数据全部加载完成...");
                }
            }
        });
    }


    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoading(boolean b) {
        isLoading = b;
    }
}
