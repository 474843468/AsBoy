package com.tmall.myredboy.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.OrderInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.recyclerview.BaseAdapter;
import com.tmall.myredboy.recyclerview.BaseLoadMoreAdapter;
import com.tmall.myredboy.recyclerview.BaseViewHolder;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

public class MyOrderActivity extends BaseActivity {

    private SwipeRefreshLayout            mRefreshLayout;
    private RecyclerView                  recyclerView;
    private TextView                      tvEmpty;
    private String                        uId;
    private List<OrderInfo.OrderListBean> mDatas;
    private MyAdapter                     mAdapter;
    private int                           orderType;
    private int pageNo   = 1;
    private int pageSize = 7;
    private boolean isEnd;
    private boolean isRefresh;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");
        tvCenter.setText("我的订单");
        tvEmpty = (TextView) findViewById(R.id.tvEmpty);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rg = (RadioGroup) findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isEnd = false;
                pageNo = 1;
                switch (checkedId) {
                    case R.id.rbLeft:
                        getDataFromServer(1);
                        break;
                    case R.id.rbCenter:
                        getDataFromServer(2);
                        break;
                    case R.id.rbRight:
                        getDataFromServer(3);
                        break;
                }
            }
        });
        rg.check(R.id.rbLeft);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pageNo = 1;
        isEnd = false;
        rg.check(R.id.rbLeft);
        getDataFromServer(1);
    }

    private void getDataFromServer(int orderType) {
        this.orderType = orderType;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("pageNo", String.valueOf(pageNo));
        requestParams.addQueryStringParameter("pageSize", String.valueOf(pageSize));
        requestParams.addQueryStringParameter("type", String.valueOf(orderType));
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "product/order_getOrderList.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseJson(responseInfo.result);
                        isRefresh = false;
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(MyOrderActivity.this, "网络正忙，请稍候...");
                        isRefresh = false;
                    }
                });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        OrderInfo orderInfo = gson.fromJson(json, OrderInfo.class);
        mDatas = orderInfo.orderList;

        mAdapter = new MyAdapter(this, recyclerView, mDatas, R.layout.item_order);
        if (mAdapter.getItemCount() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(mAdapter);
        addListener();
    }

    private void addListener() {
        //下拉刷新
        mRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.BLACK);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                refreshData();
            }
        });
        //上拉加载
        mAdapter.setOnLoadMoreListener(new BaseLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isEnd && !isRefresh) {
                    loadMore();
                }
            }
        });
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyOrderActivity.this, OrderItemActivity.class);
                intent.putExtra("data", mDatas.get(position));
                startActivity(intent);
            }
        });
    }

    //下拉刷新
    private void refreshData() {
        isEnd = false;
        pageNo = 1;
        getDataFromServer(orderType);
        // mAdapter.updateData(data);
        mRefreshLayout.setRefreshing(false);
    }

    //上拉加载数据
    private void loadMore() {
        getMoreDataFromServer(orderType);
    }

    private void getMoreDataFromServer(int orderType) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("pageNo", String.valueOf(++pageNo));
        requestParams.addQueryStringParameter("pageSize", String.valueOf(pageSize));
        requestParams.addQueryStringParameter("type", String.valueOf(orderType));
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "product/order_getOrderList.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseMoreJson(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(MyOrderActivity.this, "网络正忙，请稍候...");
                    }
                });
    }

    private void parseMoreJson(String json) {
        Gson gson = new Gson();
        OrderInfo orderInfo = gson.fromJson(json, OrderInfo.class);
        mAdapter.addAll(orderInfo.orderList);
        if (mDatas.size() < pageNo * pageSize) {
            isEnd = true;
        }
        mAdapter.setLoading(false);
    }

    class MyAdapter extends BaseLoadMoreAdapter<OrderInfo.OrderListBean> {

        public MyAdapter(Context mContext, RecyclerView recyclerView, List mDatas, int mLayoutId) {
            super(mContext, recyclerView, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, OrderInfo.OrderListBean bean) {
            holder.setText(R.id.tvNum, bean.orderId);
            holder.setText(R.id.tvTotalPrice, String.valueOf(bean.totalPrice));
            holder.setText(R.id.tvDate, bean.createTime);
            switch (bean.orderState) {
                case -1:
                    holder.setText(R.id.confirmStatus, "已取消");
                    break;
                case 2:
                    holder.setText(R.id.confirmStatus, "在途中");
                    break;
            }
        }
    }

}
