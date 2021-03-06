package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;
import com.tmall.myredboy.bean.wyy.SpecialProductsInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.ArrayList;


public class SpecialProductsActivity extends BaseActivity {

    private GridView gv;

    private int promoteId;//商品Id
    private int page = 1;//设置默认加载第几页
    private ArrayList<SpecialProductsInfo.ProductData> productList;
    private LinearLayout                               llLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_products);

        tvCenter.setText("商品列表");

        gv = (GridView) findViewById(R.id.gv);
        llLoading = (LinearLayout) findViewById(R.id.ll_load);
        llLoading.setVisibility(View.VISIBLE);

        String strId = getIntent().getStringExtra("proId");
        promoteId = Integer.parseInt(strId);

        initData();

    }

    private void initData() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("proId", "1098");
        params.addBodyParameter("pageNo", String.valueOf(page));
        params.addBodyParameter("pageSize", "9");//设置每页加载的数据数量


        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_getProductByPromoteId.html", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                parseJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show(getApplicationContext(), "网络数据获取失败...");
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        SpecialProductsInfo specialProductsInfo = gson.fromJson(json, SpecialProductsInfo.class);

        productList = specialProductsInfo.product;
        llLoading.setVisibility(View.GONE);
        MyAdapter myAdapter = new MyAdapter();
        gv.setAdapter(myAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1 = productList.get(position).id;
                Intent intent = new Intent(SpecialProductsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(id1));
                startActivity(intent);
            }
        });

        //滚动监听
        gv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { //判断当前滚动的状态
                    //如果滚到到了最后一行
                    if (view.getLastVisiblePosition() == (productList.size()) % 3) { //当滚动的条目 = 集合的最后一条
                        //重新赋值,加载下一页
                        page = ++page;
                        //获取网络数据
                        initData();
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    class MyAdapter extends BaseAdapter {

        private BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(SpecialProductsActivity.this);
        }

        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return productList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.special_item, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_common);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tvPresentPrice = (TextView) convertView.findViewById(R.id.tv_present_price);

                bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + productList.get(position).coverimg);

                holder.tvComment.setText("已有" + String.valueOf(productList.get(position).commentCount) + "人评论");
                holder.tvDes.setText(productList.get(position).name);
                holder.tvOldPrice.setText("原价: " + "￥" + productList.get(position).marketprice);
                holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                holder.tvPresentPrice.setText("现价: " + "￥" + productList.get(position).sellprice);
            }
            return convertView;

        }
    }

    static class ViewHolder {
        ImageView ivIcon;//商品图片
        TextView  tvDes;//商品名
        TextView  tvOldPrice;//原价
        TextView  tvPresentPrice;//现价
        TextView  tvComment;//评论
    }

}