package com.tmall.myredboy.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by RaphetS on 2016/9/28.
 * 普通的万能Adapter
 * 支持onItemClick
 * 支持onLongItemClick
 */
public abstract class HeaderAdapter<T> extends RecyclerView.Adapter {

    private final int TYPE_HEADER  = 0;
    private final int TYPE_CONTENT = 1;
    private final int TYPE_FOOTER  = 2;

    private Context                 mContext;
    private List<T>                 mDatas;
    private int                     mLayoutId;
    private View                    headView;
    private View                    footView;
    private OnItemClickListener     mItemClickListener;
    private onLongItemClickListener mLongItemClickListener;
    private boolean                 hasHeader;

    public HeaderAdapter(Context mContext, List<T> mDatas, int mLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mLayoutId = mLayoutId;
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (headView != null && footView != null) {
            if (position == 0) {
                return TYPE_HEADER;
            } else if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_CONTENT;
            }
        } else if (headView != null) {
            if (position == 0) {
                return TYPE_HEADER;
            } else {
                return TYPE_CONTENT;
            }
        } else if (footView != null) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return TYPE_CONTENT;
            }
        } else {
            return TYPE_CONTENT;
        }
    }

    public void addHeader(View headView) {
        this.headView = headView;
    }

    public void addFooter(View footView) {
        this.footView = footView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            HeadViewHolder headViewHolder = new HeadViewHolder(headView);
            return headViewHolder;
        } else if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            BaseViewHolder holder = new BaseViewHolder(view);
            return holder;
        } else {
            FootViewHolder footViewHolder = new FootViewHolder(footView);
            return footViewHolder;
        }
    }

    @Override
    public int getItemCount() {
        if (headView != null && footView != null) {
            return mDatas.size() + 2;
        } else if (headView != null || footView != null) {
            return mDatas.size() + 1;
        } else {
            return mDatas.size();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int newPosition = headView != null ? position - 1 : position;
        if (holder instanceof BaseViewHolder) {
            convert(mContext, holder, mDatas.get(newPosition));
            if (mItemClickListener != null) {
                ((BaseViewHolder) holder).mItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(v, newPosition);
                    }
                });
            }
            if (mLongItemClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mLongItemClickListener.onLongItemClick(v, newPosition);
                        return true;
                    }
                });
            }
        }
    }

    protected abstract void convert(Context mContext, RecyclerView.ViewHolder holder, T t);

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface onLongItemClickListener {
        void onLongItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setonLongItemClickListener(onLongItemClickListener listener) {
        this.mLongItemClickListener = listener;
    }

    //头布局holder
    public class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    //脚布局holder
    public class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}

