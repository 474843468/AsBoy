package com.tmall.myredboy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;
import com.tmall.myredboy.bean.FavoritesBean;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.recyclerview.BaseLoadMoreAdapter;
import com.tmall.myredboy.recyclerview.BaseViewHolder;
import com.tmall.myredboy.recyclerview.DividerItemDecoration;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

public class FavoritesActivity extends BaseActivity {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView       recyclerView;
    private RelativeLayout     rlEmpty;
    private String             uId;
    private int pagerNo = 1;
    private List<FavoritesBean.CollectionsBean> mDatas;
    private MyAdapter                           mAdapter;
    private FavoritesBean                       favoritesBean;
    private LinearLayout                        llLoading;
    private boolean                             isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");
        tvCenter.setText("收藏夹");
        btnRight.setText("清空本页");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFavorites();
            }
        });

        rlEmpty = (RelativeLayout) findViewById(R.id.rlEmpty);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        initData();
    }

    //清空收藏
    private void clearFavorites() {
        for (int i = 0; i < mDatas.size(); i++) {
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configTimeout(5000);
            RequestParams requestParams = new RequestParams();
            requestParams.addQueryStringParameter("uId", uId);
            requestParams.addQueryStringParameter("proId", String.valueOf(mDatas.get(i).product.id));
            final int finalI = i;
            httpUtils.send(HttpRequest.HttpMethod.POST,
                    GlobalConstants.URL_PREFIX + "productCollection/productCollection_delCollection.html",
                    requestParams, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (finalI == mDatas.size() - 1) {
                                ToastUtils.show(FavoritesActivity.this, "清空成功...");
                                getDataFromServer();
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            e.printStackTrace();
                            llLoading.setVisibility(View.GONE);
                            ToastUtils.show(FavoritesActivity.this, "网络正忙，请稍候...");
                        }
                    });
        }
    }

    private void initData() {
        llLoading.setVisibility(View.VISIBLE);
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("pageNo", "1");
        requestParams.addQueryStringParameter("pageSize", "7");
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "productCollection/productCollection_getFavourites.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseJson(responseInfo.result);
                        llLoading.setVisibility(View.GONE);
                        isRefresh = false;
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        llLoading.setVisibility(View.GONE);
                        ToastUtils.show(FavoritesActivity.this, "网络正忙，请稍候...");
                        isRefresh = false;
                    }
                });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        favoritesBean = gson.fromJson(json, FavoritesBean.class);
        mDatas = favoritesBean.collections;

        //MyAdapter mAdapter = new MyAdapter(this, mDatas, R.layout.list_item_goods);
        mAdapter = new MyAdapter(this, recyclerView, mDatas, R.layout.list_item_goods);
        if (mAdapter.getItemCount() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
        } else {
            rlEmpty.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(mAdapter);

        addListener();
    }

    private void addListener() {
        //上拉加载
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.BLACK);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                refreshData();
            }
        });
        //下拉刷新
        mAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mDatas.size() < favoritesBean.totalcount && !isRefresh) {
                    loadMore();
                    mAdapter.setLoading(true);
                }
            }
        });
        //条目点击
        mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(FavoritesActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(mDatas.get(position).product.id));
                startActivity(intent);
            }
        });
    }

    //下拉刷新
    private void refreshData() {
        pagerNo = 1;
        getDataFromServer();
        // mAdapter.updateData(data);
        mRefreshLayout.setRefreshing(false);
    }

    //上拉加载数据
    private void loadMore() {
        getMoreDataFromServer();
    }

    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("pageNo", String.valueOf(++pagerNo));
        requestParams.addQueryStringParameter("pageSize", "7");
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "productCollection/productCollection_getFavourites.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseMoreJson(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(FavoritesActivity.this, "网络正忙,请稍候重试...");
                    }
                });
    }

    private void parseMoreJson(String json) {
        Gson gson = new Gson();
        FavoritesBean favoritesBean = gson.fromJson(json, FavoritesBean.class);
        mAdapter.addAll(favoritesBean.collections);
        mAdapter.setLoading(false);
    }

    class MyAdapter extends BaseLoadMoreAdapter<FavoritesBean.CollectionsBean> {

        private final BitmapUtils bitmapUtils;

        public MyAdapter(Context mContext, RecyclerView recyclerView, List mDatas, int mLayoutId) {
            super(mContext, recyclerView, mDatas, mLayoutId);
            bitmapUtils = new BitmapUtils(mContext);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, FavoritesBean.CollectionsBean collectionsBean) {
            if (holder instanceof BaseViewHolder) {
                ImageView ivImage = ((BaseViewHolder) holder).getView(R.id.iv_goods_image);
                bitmapUtils.display(ivImage, GlobalConstants.URL_IMAGE + collectionsBean.product.coverimg);
                TextView tvDes = ((BaseViewHolder) holder).getView(R.id.tv_goods_des);
                if (TextUtils.isEmpty(collectionsBean.product.des)) {
                    tvDes.setVisibility(View.GONE);
                } else {
                    tvDes.setVisibility(View.VISIBLE);
                    tvDes.setText(collectionsBean.product.des);
                }
                ((BaseViewHolder) holder).setText(R.id.tv_goods_name, collectionsBean.product.name);
                ((BaseViewHolder) holder).setText(R.id.tv_goods_current_price, "￥" + collectionsBean.product.sellprice);
                ((BaseViewHolder) holder).setText(R.id.tv_goods_old_price, "￥" + collectionsBean.product.marketprice);
                ((BaseViewHolder) holder).setText(R.id.tv_goods_common, String.format("已有%s人评价", collectionsBean.product.commentCount));
            }
        }
    }

}
