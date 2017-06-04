package com.tmall.myredboy.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;
import com.tmall.myredboy.bean.OrderInfo;
import com.tmall.myredboy.bean.OrderItemInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.recyclerview.BaseViewHolder;
import com.tmall.myredboy.recyclerview.DividerItemDecoration;
import com.tmall.myredboy.recyclerview.HeaderAdapter;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OrderItemActivity extends BaseActivity {

    private RecyclerView                                        recyclerView;
    private String                                              uId;
    private String                                              orderId;
    private TextView                                            tvAddress;
    private TextView                                            tvOrderId;
    private TextView                                            tvOrderStatus;
    private TextView                                            tvTransWay;
    private TextView                                            tvPayWay;
    private TextView                                            tvOrderTime;
    private TextView                                            tvSendTime;
    private TextView                                            tvBillHead;
    private TextView                                            tvSendOrder;
    private List<OrderItemInfo.OrderDetailBean.ProductListBean> mDatas;
    private View                                                headerView;
    private OrderItemInfo.OrderDetailBean                       detailBean;
    private View                                                footerView;
    private TextView                                            tvCount;
    private TextView                                            tvPrice;
    private TextView                                            tvSellPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item);
        uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");

        tvCenter.setText("我的订单");
        OrderInfo.OrderListBean mData = (OrderInfo.OrderListBean) getIntent().getSerializableExtra("data");
        this.orderId = mData.orderId;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //初始化头布局
        headerView = View.inflate(this, R.layout.item_header_orderitem, null);
        headerView.findViewById(R.id.llTrans).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //物流查询
                Intent intent = new Intent(OrderItemActivity.this, TranslationActivity.class);
                intent.putExtra("data", detailBean.orderId);
                startActivity(intent);
            }
        });
        tvAddress = (TextView) headerView.findViewById(R.id.tvAddress);
        tvOrderId = (TextView) headerView.findViewById(R.id.tvOrderId);
        tvOrderStatus = (TextView) headerView.findViewById(R.id.tvOrderStatus);
        tvTransWay = (TextView) headerView.findViewById(R.id.tvTransWay);
        tvPayWay = (TextView) headerView.findViewById(R.id.tvPayWay);
        tvOrderTime = (TextView) headerView.findViewById(R.id.tvOrderTime);
        tvSendTime = (TextView) headerView.findViewById(R.id.tvSendTime);
        tvBillHead = (TextView) headerView.findViewById(R.id.tvBillHead);
        tvSendOrder = (TextView) headerView.findViewById(R.id.tvSendOrder);
        //初始化脚布局
        footerView = View.inflate(this, R.layout.item_footer_orderitem, null);
        tvCount = (TextView) footerView.findViewById(R.id.tvCount);
        tvPrice = (TextView) footerView.findViewById(R.id.tvPrice);
        tvSellPrice = (TextView) footerView.findViewById(R.id.tvSellPrice);
        footerView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消订单
                showConfirmDialog();
            }
        });
        //初始化数据
        getDataFromServer();
    }

    private void cancle() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("oId", orderId);
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "product/order_cancelOrder.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String json = responseInfo.result;
                        try {
                            JSONObject jo = new JSONObject(json);
                            String status = jo.getString("status");
                            if ("200".equals(status)) {
                                ToastUtils.show(OrderItemActivity.this, "订单取消成功...");
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(OrderItemActivity.this, "网络正忙，请稍候...");
                    }
                });
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("取消订单");
        builder.setMessage("真的要取消这个订单么？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancle();
            }
        });
        builder.show();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("oId", orderId);
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "product/order_getOrderDetail.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseJson(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(OrderItemActivity.this, "网络正忙，请稍候...");
                    }
                });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        OrderItemInfo orderItemInfo = gson.fromJson(json, OrderItemInfo.class);
        detailBean = orderItemInfo.orderDetail;
        mDatas = detailBean.productList;

        tvAddress.setText(detailBean.address);
        tvOrderId.setText(detailBean.orderId);
        switch (detailBean.orderState) {
            case -1:
                tvOrderStatus.setText("取消状态");
                break;
            case 2:
                tvOrderStatus.setText("送达状态");
                break;
        }
        //初始化头部局数据
        tvTransWay.setText(detailBean.sendWay);
        tvPayWay.setText(detailBean.paymentWay);
        tvOrderTime.setText(detailBean.createTime);
        tvSendTime.setText(detailBean.createTime);
        tvBillHead.setText(detailBean.ticketContent);
        tvSendOrder.setText(detailBean.sendTime);
        //初始化脚布局数据
        int count = 0;
        for (OrderItemInfo.OrderDetailBean.ProductListBean bean : mDatas) {
            count += bean.amount;
        }
        tvCount.setText(String.valueOf(count) + "件");
        tvPrice.setText(String.valueOf(detailBean.totalPrice));
        tvSellPrice.setText("￥" + String.format("%.2f", (detailBean.totalPrice - 30)));

        MyAdapter adapter = new MyAdapter(this, mDatas, R.layout.item_orderdetail);
        //添加头布局
        adapter.addHeader(headerView);
        //添加脚布局
        adapter.addFooter(footerView);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new HeaderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(OrderItemActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(mDatas.get(position).id));
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends HeaderAdapter<OrderItemInfo.OrderDetailBean.ProductListBean> {

        public MyAdapter(Context mContext, List<OrderItemInfo.OrderDetailBean.ProductListBean> mDatas, int mLayoutId) {
            super(mContext, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, RecyclerView.ViewHolder holder, OrderItemInfo.OrderDetailBean.ProductListBean bean) {
            if (holder instanceof BaseViewHolder) {
                ((BaseViewHolder) holder).setText(R.id.tvName, bean.name);
                RelativeLayout rlExtras = ((BaseViewHolder) holder).getView(R.id.rlExtras);
                String line = bean.extras;
                if (line != null && !line.equals("null")) {
                    rlExtras.setVisibility(View.VISIBLE);
                    String[] split = line.split("-");
                    String[] split1 = split[0].split(":");
                    String[] split2 = split[1].split(":");
                    ((BaseViewHolder) holder).setText(R.id.tvColor, split1[1]);
                    ((BaseViewHolder) holder).setText(R.id.tvSize, String.valueOf(split2[1]));
                } else {
                    rlExtras.setVisibility(View.GONE);
                }
                ((BaseViewHolder) holder).setText(R.id.tvNum, String.valueOf(bean.amount));
                ((BaseViewHolder) holder).setText(R.id.tvPrice, String.valueOf(bean.sellprice));
            }
        }
    }

}
