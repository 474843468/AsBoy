package com.tmall.myredboy.activity.lqq;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.bean.lqq.ProductList;
import com.tmall.myredboy.bean.lqq.ProductListTwo;
import com.tmall.myredboy.fragment.CategoryFragment;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;
import com.tmall.myredboy.widget.RefreshListView;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;
import static android.R.attr.startX;
import static android.R.attr.startY;
import static android.R.transition.move;
import static android.os.Build.VERSION_CODES.M;
import static com.tmall.myredboy.R.drawable.e;
import static com.tmall.myredboy.R.id.llLoading;


public class GoodsListActivity extends BaseActivity implements RefreshListView.OnRefreshListener {

    public static final int PAGE_SIZE = 7;
    public static final int ORDER_SALES_DESCEND = 1;    //销量降序
    public static final int ORDER_SALES_ASCEND= 2;      //销量升序
    public static final int ORDER_PRICE_ASCEND= 3;      //价格升序
    public static final int ORDER_PRICE_DESCEND= 4;      //价格降序
    public static final int ORDER_GOOD_COMMON_ASCEND= 5;      //好评度升序
    public static final int ORDER_GOOD_COMMON_DESCEND= 6;      //好评度降序
    public static final int ORDER_SHOW_DATA_DESCEND= 7;      //上架时间降序
    public static final int ORDER_SHOW_DATA_ASCEND= 8;      //上架时间升序
    public boolean scale = true;    //第一次点击时销量默认降序
    public boolean price = true;    //第一次点击价格默认降序
    public boolean common = true;   //第一次点击好评度默认降序
    public boolean showData = true;     //第一次点击上架时间默认降序
    private RadioGroup rg;
    private RefreshListView lvList;
    private int productOrder;
    private String id;
    public ArrayList<ProductList.Product> product;
    private ArrayList<ProductList.Product> moreProduct;
    private RadioButton rbScale;
    private RadioButton rbPrice;
    private RadioButton rbCommon;
    private RadioButton rbShowData;
    private int page = 1;
    private com.tmall.myredboy.utils.HttpUtils utils = new com.tmall.myredboy.utils.HttpUtils();
    private RelativeLayout rlGoodsList;
    private ImageView earchEmpty;
    private RelativeLayout rlGoodsActivity;
    private LinearLayout llLoading;
    private GifView gf1;
    private LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        Intent intent = getIntent();
        //获取某类商品的ID
        id = intent.getStringExtra("id");
        tvCenter.setText("商品列表");

        rg = (RadioGroup) findViewById(R.id.rg);
        lvList = (RefreshListView) findViewById(R.id.lv_list);
        rbScale = (RadioButton) findViewById(R.id.rb_scale);        //销量
        rbPrice = (RadioButton) findViewById(R.id.rb_price);        //价格
        rbCommon = (RadioButton) findViewById(R.id.rb_common);      //好评度
        rbShowData = (RadioButton) findViewById(R.id.rb_show_data);     //上架时间
        rlGoodsList = (RelativeLayout) findViewById(R.id.rl_goods_list);
        earchEmpty = (ImageView) findViewById(R.id.search_empty);
        rlGoodsActivity = (RelativeLayout) findViewById(R.id.rl_goods_activity);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
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
        llContent.setVisibility(View.GONE);
        llLoading.setVisibility(View.VISIBLE);

        rbScale.setOnClickListener(this);
        rbPrice.setOnClickListener(this);
        rbCommon.setOnClickListener(this);
        rbShowData.setOnClickListener(this);
        lvList.setOnRefreshListener(this);

        //默认销量降序
        productOrder = ORDER_SALES_DESCEND;     //scale = true;
        rg.check(R.id.rb_scale);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", product.get(position - 1).id);
                startActivity(intent);
            }
        });

        getDataFromServer();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Resources resources = this.getResources();
        Drawable arrowDown = resources.getDrawable(R.drawable.filter_arrow_down);
        Drawable arrowUp = resources.getDrawable(R.drawable.filter_arrow_up);
        int clickId = v.getId();
        rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        switch (clickId) {
            case R.id.rb_scale:
                rg.clearCheck();
                rg.check(R.id.rb_scale);

                if(scale) {
                    productOrder = ORDER_SALES_DESCEND; //销量降序
                    rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    scale = false;
                }else {
                    if(productOrder == ORDER_SALES_DESCEND) {
                        productOrder = ORDER_SALES_ASCEND;  //销量升序
                        rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }else {
                        productOrder = ORDER_SALES_DESCEND; //销量降序
                        rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    }
                }
                break;
            case R.id.rb_price:
                rg.clearCheck();
                rg.check(R.id.rb_price);
//                rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                if(price) {
                    productOrder = ORDER_PRICE_DESCEND;  //价格降序
                    rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null,arrowDown, null);
                    price = false;
                }else {
                    if(productOrder == ORDER_PRICE_ASCEND) { //价格升序
                        productOrder = ORDER_PRICE_DESCEND;  //价格降序
                      rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null,arrowDown, null);
                    }else {
                        productOrder = ORDER_PRICE_ASCEND; //价格升序
                      rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }
                }
                break;
            case R.id.rb_common:
                rg.clearCheck();
                rg.check(R.id.rb_common);
//                rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                rbScale.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//                rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                if(common) {
                    productOrder = ORDER_GOOD_COMMON_DESCEND;  //好评度降序
                    rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    common = false;
                }else {
                    if(productOrder == ORDER_GOOD_COMMON_ASCEND) { //好评度升序
                        productOrder = ORDER_GOOD_COMMON_DESCEND;  //好评度降序
                        rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }else {
                        productOrder = ORDER_GOOD_COMMON_ASCEND; //好评度升序
                        rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    }
                }
                break;
            case R.id.rb_show_data:
                rg.clearCheck();
                rg.check(R.id.rb_show_data);
//                rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//
//                rbCommon.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                if(showData) {
                    productOrder = ORDER_SHOW_DATA_DESCEND; //上架时间降序
                    rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    showData = false;
                }else {
                    if(productOrder == ORDER_SHOW_DATA_DESCEND) { //上架时间降序
                        productOrder = ORDER_SHOW_DATA_ASCEND;  //上架时间升序
                        rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
                    }else {
                        productOrder = ORDER_SHOW_DATA_DESCEND; //上架时间降序
                        rbShowData.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
                    }
                }
                break;
        }
        //重新请求网络数据
        getDataFromServer();
        //更新ListV集合
        lvList.deferNotifyDataSetChanged();
    }

    public void getDataFromServer() {
        String url = GlobalConstants.URL_PREFIX + GlobalConstants.PRODUCT_LIST
                + "?pageNo=" + page +
                "&pageSize=" + PAGE_SIZE + "&categoryid=" + id + "&order=" + productOrder;

        com.tmall.myredboy.utils.HttpUtils.getHttpDes(GoodsListActivity.this, url, ProductList.class, new com.tmall.myredboy.utils.HttpUtils.OnHttpSuccess() {
            @Override
            public void onSuccess(Object object) {

                llContent.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);

                //object是解析好的对
                ProductList productList = (ProductList)object;
                //获取分类浏览页面的字条目
                product = productList.product;

                //给ListView设置适配器
                MyListAdapter myListAdapter = new MyListAdapter();
                lvList.setAdapter(myListAdapter);
                if(myListAdapter.getCount() == 0) {
                    //没有数据,显示空图
                    earchEmpty.setVisibility(View.VISIBLE);
//                    rlGoodsActivity.setVisibility(View.GONE);
                    return;
                }else {
                    earchEmpty.setVisibility(View.GONE);
                    rlGoodsActivity.setVisibility(View.VISIBLE);
                }

                lvList.onRefreshComplete(true);
            }
        });

    }

    public void getMoreDataFromServer() {
        page += 1;
        String url = GlobalConstants.URL_PREFIX + GlobalConstants.PRODUCT_LIST
                + "?pageNo=" + page +
                "&pageSize=" + PAGE_SIZE + "&categoryid=" + id + "&order=" + productOrder;

        com.tmall.myredboy.utils.HttpUtils.getHttpDes(GoodsListActivity.this, url, ProductList.class, new com.tmall.myredboy.utils.HttpUtils.OnHttpSuccess() {
            @Override
            public void onSuccess(Object object) {
                //object是解析好的对
                ProductList moreProductList = (ProductList)object;
                //获取分类浏览页面的字条目
                product.addAll(moreProductList.product);;
                //给ListView设置适配器
                lvList.setAdapter(new MyListAdapter());

                lvList.onMoreDataComplete();
            }
        });

    }

    //下拉刷新
    @Override
    public void onRefresh() {
        System.out.println("刷新");
        //更新数据
        getDataFromServer();
    }

    //上拉加载更多
    @Override
    public void onLoadMore() {
        getMoreDataFromServer();
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            System.out.println("product.size()" + product.size());
            return product.size();
        }

        @Override
        public Object getItem(int position) {
            return product.get(position);
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
                convertView = View.inflate(GoodsListActivity.this, R.layout.list_item_goods, null);
                mHolder.ivGoodsImage = (ImageView) convertView.findViewById(R.id.iv_goods_image);
                mHolder.tvGoodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
                mHolder.tvGoodsDes = (TextView) convertView.findViewById(R.id.tv_goods_des);
                mHolder.tvGoodsCurrentPrice = (TextView) convertView.findViewById(R.id.tv_goods_current_price);
                mHolder.tvGoodsOldPrice = (TextView) convertView.findViewById(R.id.tv_goods_old_price);
                mHolder.tvGoodsCommon = (TextView) convertView.findViewById(R.id.tv_goods_common);
                mHolder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);

                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            //设置数据
            String imageUrl = GlobalConstants.URL_IMAGE + product.get(position).coverimg;
            com.tmall.myredboy.utils.HttpUtils.viewSetImage(GoodsListActivity.this,imageUrl,mHolder.ivGoodsImage);
            mHolder.tvGoodsName.setText(product.get(position).name);
            mHolder.tvGoodsCurrentPrice.setText(product.get(position).sellprice);
            mHolder.tvGoodsOldPrice.setText(product.get(position).marketprice);
            mHolder.tvGoodsCommon.setText("已有" + product.get(position).commentCount + "人评论");

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivGoodsImage;
        public TextView tvGoodsName;
        public TextView tvGoodsDes;
        public TextView tvGoodsCurrentPrice;
        public TextView tvGoodsOldPrice;
        public TextView tvGoodsCommon;
        public ImageView ivArrow;
    }

}
