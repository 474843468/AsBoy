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
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;
import com.tmall.myredboy.bean.wyy.HotProductsInfo;
import com.tmall.myredboy.global.GlobalConstants;

import java.util.List;

public class StarActivity extends BaseActivity {

    private GridView gvList;

    private HotProductsInfo                    hotProductsInfo;
    private MyAdapter                          myAdapter;
    private List<HotProductsInfo.ProductsBean> hotProductsList;
    private int page = 1;//默认第一页
    private LinearLayout llLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        tvCenter.setText("热门单品");
        gvList = (GridView) findViewById(R.id.gv_list);
        llLoading = (LinearLayout) findViewById(R.id.ll_load);
        llLoading.setVisibility(View.VISIBLE);

        initData();
        //gridView滚动监听
        gvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //判断
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) { //判断当前滚动的状态
                    //如果滚到到了最下面一行
                    if (view.getLastVisiblePosition() == hotProductsList.size() - 1) { //当滚动的条目 = 集合的最后一条
                        //从新赋值
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
        gvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int StarId = hotProductsList.get(position).id;
                Intent intent = new Intent(StarActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(StarId));
                startActivity(intent);
            }
        });
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();                            // + "?pageNo=" + 1 + "&pageSize=" + 7
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_findHotProduct.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("StarActivity: " + json);
                parseJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        hotProductsInfo = gson.fromJson(json, HotProductsInfo.class);

        hotProductsList = hotProductsInfo.products;
        llLoading.setVisibility(View.GONE);

        myAdapter = new MyAdapter();
        gvList.setAdapter(myAdapter);

    }


    class MyAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(StarActivity.this);
        }

        @Override
        public int getCount() {
            return hotProductsList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotProductsList.get(position);
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
                convertView = View.inflate(StarActivity.this, R.layout.new_products_and_star_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_common);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tvPresentPrice = (TextView) convertView.findViewById(R.id.tv_present_price);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + hotProductsList.get(position).coverimg);

            holder.tvComment.setText("已有" + String.valueOf(hotProductsList.get(position).commentCount) + "人评论");
            holder.tvDes.setText(hotProductsList.get(position).name);
            holder.tvOldPrice.setText("原价: " + "￥" + hotProductsList.get(position).marketprice);
            holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            holder.tvPresentPrice.setText("现价: " + "￥" + hotProductsList.get(position).sellprice);

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
