package com.tmall.myredboy.activity.wyy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.bean.wyy.PromoteInfo;
import com.tmall.myredboy.global.GlobalConstants;

import java.util.List;


public class PromoteActivity extends BaseActivity {

    private ListView                      lvList;
    private MyAdapter                     myAdapter;
    private List<PromoteInfo.PromoteBean> promoteList;
    private PromoteInfo                   promoteInfo;
    private LinearLayout                  llLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);

        tvCenter.setText("促销快报");

        lvList = (ListView) findViewById(R.id.lv_list);
        llLoading = (LinearLayout) findViewById(R.id.ll_load);
        llLoading.setVisibility(View.VISIBLE);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String promoteId = promoteList.get(position).promoteId;
                Intent intent = new Intent(PromoteActivity.this, SpecialProductsActivity.class);
                intent.putExtra("proId", promoteId);
                startActivity(intent);
            }
        });

        initData();
    }

    public void initData() {
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_getPromote.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("PromoteActivity: " + json);
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
        promoteInfo = gson.fromJson(json, PromoteInfo.class);
        System.out.println("PromoteActivity:productSearchInfo: " + json);

        promoteList = promoteInfo.promote;
        llLoading.setVisibility(View.GONE);

        myAdapter = new MyAdapter();
        lvList.setAdapter(myAdapter);
    }

    class MyAdapter extends BaseAdapter {
        private BitmapUtils bitmapUtils;

        public MyAdapter() {
            bitmapUtils = new BitmapUtils(PromoteActivity.this);
        }

        @Override
        public int getCount() {
            return promoteList.size();
        }

        @Override
        public Object getItem(int position) {
            return promoteList.get(position);
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
                convertView = View.inflate(PromoteActivity.this, R.layout.promote_item_list, null);

                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                holder.ivPromote = (ImageView) convertView.findViewById(R.id.iv_promote);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvTitle.setText(promoteList.get(position).promoteName);
            holder.tvDate.setText(promoteList.get(position).time);
            bitmapUtils.display(holder.ivPromote, GlobalConstants.URL_IMAGE + promoteList.get(position).promoteCoverImg);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPromote;  //专题图片
        public TextView  tvTitle;    //专题名称
        public TextView  tvDate;     //专题时间
    }

}
