package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.tmall.myredboy.bean.wyy.ProductSearchInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.global.Globals;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.widget.RefreshListView;

import java.util.List;

public class SearchResultActivity extends BaseActivity implements RefreshListView.OnRefreshListener {

    private RefreshListView                     lvList;
    private MyAdapter                           myAdapter;
    private List<ProductSearchInfo.ProductBean> productList;
    private ProductSearchInfo                   productSearchInfo;
    private ProgressBar                         pb;
    private LinearLayout                        llLoad;
    private LinearLayout                        llSearchEmpty;

    private              int PRODUCT_ORDER     = ORDER_SALE_ASC;
    private static final int ORDER_SALE_ASC    = 1;
    private static final int ORDER_SALE_DESC   = 2;
    private static final int ORDER_PRICE_ASC   = 3;
    private static final int ORDER_PRICE_DESC  = 4;
    private static final int ORDER_PRAISE_ASC  = 5;
    private static final int ORDER_PRAISE_DESC = 6;
    private static final int ORDER_TIME_ASC    = 7;
    private static final int ORDER_TIME_DESC   = 8;

    private static final int PAGE_SIZE = 7;

    private int PAGE_NO = 1;
    private RadioGroup  rg;
    private RadioButton rbSale;
    private RadioButton rbPrice;
    private RadioButton rbTime;
    private RadioButton rbPraise;
    private String      keyword;
    public boolean sale   = true;    //第一次点击时销量默认降序
    public boolean price  = true;    //第一次点击价格默认降序
    public boolean praise = true;   //第一次点击好评度默认降序
    public boolean times  = true;     //第一次点击上架时间默认降序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        llLoad = (LinearLayout) findViewById(R.id.ll_load);

        pb = (ProgressBar) findViewById(R.id.pb);

        rg = (RadioGroup) findViewById(R.id.rg);
        rbSale = (RadioButton) findViewById(R.id.rb_sale);
        rbPrice = (RadioButton) findViewById(R.id.rb_price);
        Drawable icon = getResources().getDrawable(R.drawable.filter_arrow_up);
        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        rbPraise = (RadioButton) findViewById(R.id.rb_praise);
        rbTime = (RadioButton) findViewById(R.id.rb_time);

        //默认销量降序排列
        PRODUCT_ORDER = ORDER_SALE_DESC;//sale = true;
        rg.check(R.id.rb_price);

        tvCenter.setText("搜索结果( " + 0 + "条 )");

        lvList = (RefreshListView) findViewById(R.id.lv_list);
        llSearchEmpty = (LinearLayout) findViewById(R.id.ll_search_empty);

        keyword = getIntent().getStringExtra("data");

        llLoad.setVisibility(View.VISIBLE);

        rbSale.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
        rbPraise.setOnClickListener(this);
        rbTime.setOnClickListener(this);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1 = productList.get(position - 1).id;
                Intent intent = new Intent(SearchResultActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(id1));
                startActivity(intent);
            }
        });
        lvList.setOnRefreshListener(this);

        initData();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int vId = v.getId();
        Resources resources = this.getResources();
        Drawable arrowDown = resources.getDrawable(R.drawable.filter_arrow_down);
        Drawable arrowUp = resources.getDrawable(R.drawable.filter_arrow_up);
        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbSale.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbPraise.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        switch (vId) {
            case R.id.rb_sale:
                rg.clearCheck();
                rg.check(R.id.rb_sale);
                if (sale) {
                    //降序排列
                    PRODUCT_ORDER = ORDER_SALE_DESC;
                    rbSale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    sale = false;
                } else {
                    if (PRODUCT_ORDER == ORDER_SALE_DESC) {
                        PRODUCT_ORDER = ORDER_SALE_ASC;
                        rbSale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    } else {
                        PRODUCT_ORDER = ORDER_SALE_DESC;
                        rbSale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    }
                }

                break;

            case R.id.rb_price:
                rg.clearCheck();
                rg.check(R.id.rb_price);

                if (price) {
                    //价格降序
                    PRODUCT_ORDER = ORDER_PRICE_DESC;
                    rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    price = false;
                } else {
                    if (PRODUCT_ORDER == ORDER_PRICE_ASC) {
                        PRODUCT_ORDER = ORDER_PRICE_DESC;
                        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    } else {
                        PRODUCT_ORDER = ORDER_PRICE_ASC;
                        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }
                }
                break;

            case R.id.rb_praise:
                rg.clearCheck();
                rg.check(R.id.rb_praise);
                if (praise) {
                    PRODUCT_ORDER = ORDER_PRAISE_DESC;
                    rbPraise.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    praise = false;
                } else {

                    if (PRODUCT_ORDER == ORDER_PRAISE_ASC) {
                        PRODUCT_ORDER = ORDER_PRAISE_DESC;
                        rbPraise.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    } else {
                        PRODUCT_ORDER = ORDER_PRAISE_ASC;
                        rbPraise.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }
                }
                break;

            case R.id.rb_time:
                rg.clearCheck();
                rg.check(R.id.rb_time);
                if (times) {
                    PRODUCT_ORDER = ORDER_TIME_DESC;
                    rbTime.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    times = false;
                } else {

                    if (PRODUCT_ORDER == ORDER_TIME_DESC) {
                        PRODUCT_ORDER = ORDER_TIME_ASC;
                        rbTime.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    } else {
                        PRODUCT_ORDER = ORDER_TIME_DESC;
                        rbTime.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    }
                }
                break;
        }
        initData();
        //更新集合
        lvList.deferNotifyDataSetChanged();
    }

    private void initData() {
        getDataFromServer();
    }


    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addBodyParameter("keyword", keyword);
        params.addBodyParameter("pageNo", String.valueOf(PAGE_NO));
        params.addBodyParameter("pageSize", String.valueOf(PAGE_SIZE));
        params.addBodyParameter("order", String.valueOf(PRODUCT_ORDER));

        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_search.html", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("SearchResultActivity: " + json);
                parseJson(json);
                //数据加载完毕
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                lvList.onRefreshComplete(true);
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        productSearchInfo = gson.fromJson(json, ProductSearchInfo.class);

        tvCenter.setText("搜索结果( " + productSearchInfo.totalcount + "条 )");

        productList = this.productSearchInfo.product;

        llLoad.setVisibility(View.GONE);
        myAdapter = new MyAdapter();
        lvList.setAdapter(myAdapter);
        if (myAdapter.getCount() == 0) {
            llSearchEmpty.setVisibility(View.VISIBLE);
        } else {
            llSearchEmpty.setVisibility(View.GONE);
            lvList.setVisibility(View.VISIBLE);
        }

        myAdapter.notifyDataSetChanged();
    }


    class MyAdapter extends BaseAdapter {

        private BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(SearchResultActivity.this);
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
                holder = new ViewHolder();
                convertView = View.inflate(SearchResultActivity.this, R.layout.search_result_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tvDiscuss = (TextView) convertView.findViewById(R.id.tv_discuss);
                holder.tvSale = (TextView) convertView.findViewById(R.id.tv_sale);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + productList.get(position).coverimg);
            holder.tvDes.setText(productList.get(position).name);
            holder.tvPrice.setText("￥" + productList.get(position).sellprice);
            holder.tvOldPrice.setText("￥" + productList.get(position).marketprice);
            holder.tvDiscuss.setText("已有" + productList.get(position).commentCount + "人评价");

            holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            return convertView;
        }
    }

    static class ViewHolder {
        public  ImageView ivIcon;
        public  TextView  tvDes;
        private TextView  tvSale;
        public  TextView  tvPrice;
        public  TextView  tvOldPrice;
        public  TextView  tvDiscuss;
        public  ImageView ivArrow;
    }


    public void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_search.html" + "?keywowrd=" + 1223 + "&order=" + PRODUCT_ORDER + "&pageSize=" + PAGE_SIZE + "&pageNo=" + PAGE_NO++, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("SearchResultActivity: " + json);
                parseMoreJson(json);
                //数据加载完毕
                lvList.onMoreDataComplete();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                lvList.onMoreDataComplete();
            }
        });
    }

    private void parseMoreJson(String json) {
        Gson gson = new Gson();
        ProductSearchInfo moreProductSearchInfo = gson.fromJson(json, ProductSearchInfo.class);
        productList.addAll(moreProductSearchInfo.product);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        System.out.println("数据刷新了...");
        initData();
    }

    @Override
    public void onLoadMore() {
        System.out.println("数据刷新了...");
        getMoreDataFromServer();
    }
}
