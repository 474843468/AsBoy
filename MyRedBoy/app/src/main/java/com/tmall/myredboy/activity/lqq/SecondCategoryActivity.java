package com.tmall.myredboy.activity.lqq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.bean.lqq.ProductList;
import com.tmall.myredboy.bean.lqq.ProductListOne;
import com.tmall.myredboy.bean.lqq.ProductListTwo;
import com.tmall.myredboy.fragment.CategoryFragment;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.global.Globals;

import java.util.ArrayList;

import static com.tmall.myredboy.R.id.llLoading;

/**
 * LQQ 2016/11/14
 */

public class SecondCategoryActivity extends BaseActivity {

    private ListView lvList;
    private String pid;
    private ArrayList<ProductListTwo.Categories> categoriesList;
    private MyListAdapter myListAdapter;
    private String name;
    private LinearLayout llLoading;
    private GifView gf1;
    private LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_category);

        Intent intent = getIntent();
        pid = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        tvCenter.setText(name);
        tvCenter.setVisibility(View.VISIBLE);

        lvList = (ListView) findViewById(R.id.lv_list);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        // 从xml中得到GifView的句柄
        gf1 =  (GifView) llLoading.findViewById(R.id.gif1);
        // 设置Gif图片源
        gf1.setGifImage(R.drawable.baby);
        // 添加监听器
        gf1.setOnClickListener(this);
        // 设置显示的大小，拉伸或者压缩
        gf1.setShowDimension(650, 500);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        gf1.setGifImageType(GifView.GifImageType.COVER);
        lvList.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);


        getDataFromServer();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //判断是不是有字条目,如果有就跳入二级分类页面,如果没有就就跳到商品列表页面
                int flag = categoriesList.get(position).isLeaf;
                Intent intent = new Intent();
                if (flag == 1) {
                    //没有字条目,跳到商品列表页面
                    intent.putExtra("id", categoriesList.get(position).id);
                    intent.setClass(SecondCategoryActivity.this, GoodsListActivity.class);
                    startActivity(intent);
                } else {
                    //0为不是叶子节点，有子条目,点击后重新获取数据,更新ListView
                    pid = categoriesList.get(position).id;
                    tvCenter.setText(categoriesList.get(position).name);
                    getDataFromServer();
                    //刷新ListView
                    myListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void getDataFromServer() {

        String url = Globals.URL_PREFIX + GlobalConstants.PRODUCT_FIND_CATORIES + "?pid=" + pid;
        com.tmall.myredboy.utils.HttpUtils.getHttpDes(SecondCategoryActivity.this, url, ProductListTwo.class, new com.tmall.myredboy.utils.HttpUtils.OnHttpSuccess() {
            @Override
            public void onSuccess(Object object) {

                lvList.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);

                //object是解析好的对象
                ProductListTwo productListTwo = (ProductListTwo)object;
                //获取分类浏览页面的字条目
                categoriesList = productListTwo.categories;
                //给ListView设置适配器
                myListAdapter = new MyListAdapter();
                lvList.setAdapter(myListAdapter);
            }
        });
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categoriesList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoriesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(SecondCategoryActivity.this, R.layout.list_item_category, null);
                mHolder.tvTitle2 = (TextView) convertView.findViewById(R.id.tv_title2);
                mHolder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.tvTitle2.setVisibility(View.VISIBLE);

            mHolder.tvTitle2.setText(categoriesList.get(position).name);
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle2;
        public ImageView ivArrow;
    }

}
