package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
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
import com.tmall.myredboy.bean.wyy.NewProductsInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

public class NewsProductsActivity extends BaseActivity {

    private GridView                           gvList;
    private MyAdapter                          myAdapter;
    private NewProductsInfo                    newProductsInfo;
    private List<NewProductsInfo.ProductsBean> newProductsList;
    private int page = 1;//默认第一页
    private LinearLayout llLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_products);
        tvCenter.setText("新品上架");

        gvList = (GridView) findViewById(R.id.gv_list);
        llLoading = (LinearLayout) findViewById(R.id.ll_load);

        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1 = newProductsList.get(position).id;
                Intent intent = new Intent(NewsProductsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(id1));
                startActivity(intent);
            }
        });
        llLoading.setVisibility(View.VISIBLE);

        initData();

        gvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { //判断当前滚动的状态
                    //如果滚到到了最下面一行
                    if (view.getLastVisiblePosition() == newProductsList.size() - 1) { //当滚动的条目 = 集合的最后一条
                        //加载下一页
                        page = ++page;
                        //再次调用获取网络数据
                        initData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);

        RequestParams params = new RequestParams();
        params.addBodyParameter("pageNo", String.valueOf(page));
        params.addBodyParameter("pageSize", "9");//设置每页展示的个数
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_findNewProduct.html", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("LimitTimeActivity: " + json);
                parseJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show(getApplicationContext(), "获取网络数据失败...");
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        newProductsInfo = gson.fromJson(json, NewProductsInfo.class);

        newProductsList = newProductsInfo.products;
        llLoading.setVisibility(View.GONE);

        myAdapter = new MyAdapter();
        gvList.setAdapter(myAdapter);
    }


    class MyAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(NewsProductsActivity.this);
        }

        @Override
        public int getCount() {
            return newProductsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newProductsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(NewsProductsActivity.this, R.layout.new_products_and_star_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_common);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tvPresentPrice = (TextView) convertView.findViewById(R.id.tv_present_price);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + newProductsList.get(position).coverimg);

            holder.tvComment.setText("已有" + String.valueOf(newProductsList.get(position).commentCount) + "人评论");
            holder.tvDes.setText(newProductsList.get(position).name);
            holder.tvOldPrice.setText("原价: " + "￥" + newProductsList.get(position).marketprice);
            holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.tvPresentPrice.setText("现价: " + "￥" + newProductsList.get(position).sellprice);

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;//商品图片
        public TextView  tvDes;//商品名
        public TextView  tvOldPrice;//原价
        public TextView  tvPresentPrice;//现价
        public TextView  tvComment;//评论
    }
}
