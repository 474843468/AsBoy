package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.tmall.myredboy.bean.wyy.RecommendInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.global.Globals;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

public class RecommendActivity extends BaseActivity {

    private TextView                                   tvTitle1;
    private TextView                                   tvTitle2;
    private GridView                                   gv1;
    private GridView                                   gv2;
    private List<RecommendInfo.BrandBean>              brandList;
    private List<RecommendInfo.BrandBean.ChildrenBean> childrenList1;
    private List<RecommendInfo.BrandBean.ChildrenBean> childrenList2;

    private LinearLayout llLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        tvCenter.setText("推荐品牌");

        tvTitle1 = (TextView) findViewById(R.id.tv_title1);
        tvTitle2 = (TextView) findViewById(R.id.tv_title2);
        gv1 = (GridView) findViewById(R.id.gv1);
        gv2 = (GridView) findViewById(R.id.gv2);
        llLoading = (LinearLayout) findViewById(R.id.ll_load);
        llLoading.setVisibility(View.VISIBLE);

        initData();
    }

    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.URL_PREFIX + "brand/brand_getBrands.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("RecommendActivity: " + json);
                parseJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show(getApplicationContext(), "网络异常...");
                e.printStackTrace();
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        RecommendInfo recommendInfo = gson.fromJson(json, RecommendInfo.class);
        brandList = recommendInfo.brand;
        llLoading.setVisibility(View.GONE);

        childrenList1 = brandList.get(0).children;
        tvTitle1.setText(brandList.get(0).title);
        gv1.setAdapter(new MyAdapter1());


        childrenList2 = brandList.get(1).children;
        tvTitle2.setText(brandList.get(1).title);
        gv2.setAdapter(new MyAdapter2());

        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1 = childrenList1.get(position).id;
                Intent intent = new Intent(RecommendActivity.this, BrandProductsActivity.class);
                intent.putExtra("bId", String.valueOf(id1));
                startActivity(intent);
            }
        });

        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id2 = childrenList2.get(position).id;
                Intent intent = new Intent(RecommendActivity.this, BrandProductsActivity.class);
                intent.putExtra("bId", String.valueOf(id2));
                startActivity(intent);
            }
        });
    }


    class MyAdapter1 extends BaseAdapter {
        private BitmapUtils bitmapUtils;

        public MyAdapter1() {
            bitmapUtils = new BitmapUtils(RecommendActivity.this);
        }

        @Override
        public int getCount() {
            return childrenList1.size();

        }

        @Override
        public Object getItem(int position) {
            return childrenList1.get(position);
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
                convertView = View.inflate(RecommendActivity.this, R.layout.recommend_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + childrenList1.get(position).brandUrl);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivIcon;
    }

    class MyAdapter2 extends BaseAdapter {
        private BitmapUtils bitmapUtils;

        public MyAdapter2() {
            bitmapUtils = new BitmapUtils(RecommendActivity.this);
        }

        @Override
        public int getCount() {
            return childrenList2.size();

        }

        @Override
        public Object getItem(int position) {
            return childrenList2.get(position);
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
                convertView = View.inflate(RecommendActivity.this, R.layout.recommend_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + childrenList2.get(position).brandUrl);
            return convertView;
        }
    }

}