package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.tmall.myredboy.bean.wyy.LimitTimeInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;
import com.tmall.myredboy.widget.RefreshListView;
import com.tmall.myredboy.widget.TimerTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class LimitTimeActivity extends BaseActivity implements RefreshListView.OnRefreshListener {

    private RefreshListView                 lvList;
    private MyAdapter                       myAdapter;
    private LimitTimeInfo                   productSearchInfo;
    private List<LimitTimeInfo.ProductBean> productsList;

    private static final int PAGE_SIZE = 7;
    private              int PAGE_NO   = 1;
    private LinearLayout llLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit_time);

        tvCenter.setText("限时抢购");

        lvList = (RefreshListView) findViewById(R.id.lv_list);
        llLoad = (LinearLayout) findViewById(R.id.ll_load);
        llLoad.setVisibility(View.VISIBLE);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id1 = productsList.get(position - 1).id;
                Intent intent = new Intent(LimitTimeActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", String.valueOf(id1));
                startActivity(intent);
            }
        });

        lvList.setOnRefreshListener(this);

        initData();

    }


    public void initData() {

        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);

        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.URL_PREFIX + "product/product_findEmpireProduct.html" + "?pageNo=" + PAGE_NO + "&pageSize=" + PAGE_SIZE, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("LimitTimeActivity: " + json);
                parseJson(json);

                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.show(getApplicationContext(), "网络数据获取失败...");
                lvList.onRefreshComplete(false);
            }
        });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        productSearchInfo = gson.fromJson(json, LimitTimeInfo.class);
        System.out.println("LimitTimeActivity:productSearchInfo: " + json);

        productsList = productSearchInfo.products;
        llLoad.setVisibility(View.GONE);
        myAdapter = new MyAdapter();
        lvList.setAdapter(myAdapter);
    }

    public void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_findEmpireProduct.html" + "&pageSize=" + PAGE_SIZE + "?pageNo=" + PAGE_NO++, new RequestCallBack<String>() {
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
        LimitTimeInfo moreLimitTimeInfo = gson.fromJson(json, LimitTimeInfo.class);
        productsList.addAll(moreLimitTimeInfo.products);
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

    private Date date1;

    class MyAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;
        private Timer       timer;
        private int reclen = 11;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(LimitTimeActivity.this);
        }

        @Override
        public int getCount() {
            return productsList.size();
        }


        @Override
        public Object getItem(int position) {
            return productsList.get(position);
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
                convertView = View.inflate(LimitTimeActivity.this, R.layout.limit_time_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvDes = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvOldPrice = (TextView) convertView.findViewById(R.id.tv_old_price);
                holder.tvLimitTimePrice = (TextView) convertView.findViewById(R.id.tv_limit_time_price);
                holder.tvSpareTime = (TimerTextView) convertView.findViewById(R.id.tv_spare_time);
                holder.limitTimePrice = (TextView) convertView.findViewById(R.id.limit_time_price);
                holder.spareTime = (TextView) convertView.findViewById(R.id.spare_time);
                holder.tvSale = (TextView) convertView.findViewById(R.id.tv_sale);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtils.display(holder.ivIcon, GlobalConstants.URL_IMAGE + productsList.get(position).coverimg);
            holder.tvDes.setText(productsList.get(position).name);
            holder.tvOldPrice.setText("￥" + productsList.get(position).marketprice);
            holder.tvLimitTimePrice.setText("￥" + productsList.get(position).sellprice);
            holder.limitTimePrice.setText("限时特价: ");
            holder.spareTime.setText("剩余时间: ");

            holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            //            //holder
            //            final ViewHolder mHolder = holder;
            //            String previrousTime = productsList.get(position).empireTime;
            //            timer = new Timer();
            //            timer.schedule(new TimerTask() {
            //                @Override
            //                public void run() {
            //                    runOnUiThread(new Runnable() {
            //                        @Override
            //                        public void run() {
            //                            mHolder.tvSpareTime.setText("" + reclen--);
            //                            if (reclen < 0) {
            //                                timer.cancel();
            //                            }
            //                        }
            //                    });
            //                }
            //            }, 1000, 1000);
            //            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //            Date currentTime = new Date();
            //            try {
            //                date1 = sdf.parse(previrousTime);
            //            } catch (ParseException e) {
            //                e.printStackTrace();
            //            }
            //            long empireTime = currentTime.getTime() - date1.getTime();
            //            String formatCurrentTime = sdf.format(empireTime);
            //网络时间
            String empireTime = productsList.get(position).empireTime;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //当前时间
            Date mCurrentTime = new Date();
            //目标时间
            Date target = null;
            try {
                target = sdf.parse(empireTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //时间差
            long diff = target.getTime() - mCurrentTime.getTime();
            //设置时间
            holder.tvSpareTime.setTimes(diff);
            //开始倒计时
            if (!holder.tvSpareTime.isRun()) {
                holder.tvSpareTime.start();
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView     ivIcon;//封面图片
        public TextView      tvDes;//商品名称
        public TextView      tvSale;
        public TextView      tvOldPrice;//市场价
        public TextView      tvLimitTimePrice; //售价
        public TimerTextView tvSpareTime; //限时抢购截止时间
        public TextView      limitTimePrice;
        public TextView      spareTime;
        public ImageView     ivArrow;
        public Button        btnShop;
    }


}
