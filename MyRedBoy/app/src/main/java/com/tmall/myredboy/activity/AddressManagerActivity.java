package com.tmall.myredboy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.AddressInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.recyclerview.BaseAdapter;
import com.tmall.myredboy.recyclerview.BaseViewHolder;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.SnackbarUtil;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;


public class AddressManagerActivity extends BaseActivity {

    private RecyclerView                  recyclerView;
    private boolean                       isSelect;
    private RelativeLayout                rlEmpty;
    private List<AddressInfo.AddressBean> mAddress;
    private MyAdapter                     mAdapter;
    private String                        uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");
        isSelect = getIntent().getBooleanExtra("isSelect", false);
        if (isSelect) {
            tvCenter.setText("地址列表");
        } else {
            tvCenter.setText("地址管理");
        }
        btnRight.setText("新增地址");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressManagerActivity.this, AddressUpdateActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rlEmpty = (RelativeLayout) findViewById(R.id.rlEmpty);

        initData();
    }

    private void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "address/address_list.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseJson(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.show(AddressManagerActivity.this, "网络正忙,请稍候重试...");
            }
        });
    }

    private void parseJson(String json) {
        //解析数据
        Gson gson = new Gson();
        AddressInfo mAddressInfo = gson.fromJson(json, AddressInfo.class);
        mAddress = mAddressInfo.address;

        mAdapter = new MyAdapter(this, mAddress, R.layout.item_address);
        if (mAdapter.getItemCount() == 0) {
            rlEmpty.setVisibility(View.VISIBLE);
        } else {
            rlEmpty.setVisibility(View.GONE);
            if (!isSelect) {
                SnackbarUtil.show(getWindow().getDecorView(), "点击地址标签可用进行修改哦(⊙o⊙)...");
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        //点击修改
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AddressInfo.AddressBean info = mAddress.get(position);
                if (isSelect) {
                    Intent intent = new Intent();
                    intent.putExtra("data", info);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent(AddressManagerActivity.this, AddressUpdateActivity.class);
                    intent.putExtra("isUpdate", true);
                    intent.putExtra("data", info);
                    startActivity(intent);
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter<AddressInfo.AddressBean> {

        public MyAdapter(Context mContext, List<AddressInfo.AddressBean> mDatas, int mLayoutId) {
            super(mContext, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, AddressInfo.AddressBean addressBean) {
            holder.setText(R.id.tvName, addressBean.name);
            holder.setText(R.id.tvPhone, addressBean.telphone);
            holder.setText(R.id.tvAddress, addressBean.address + "\n" + addressBean.area);
            holder.setVisible(R.id.ivCheck, addressBean.isDefault == 1);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!isSelect) {
            SnackbarUtil.show(getWindow().getDecorView(), "点击地址标签可用进行修改哦(⊙o⊙)...");
        }
        getDataFromServer();
    }

}
