package com.tmall.myredboy.activity.lqq;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.activity.MainActivity;
import com.tmall.myredboy.bean.CartInfoUpdateEvent;
import com.tmall.myredboy.bean.lqq.ProductDetails;
import com.tmall.myredboy.db.dao.ScanLogDao;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.global.Globals;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;
import com.tmall.myredboy.widget.MyGallery;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import more_activity.AccountActivity;

public class GoodsDetailActivity extends BaseActivity {


    private String                           proId;
    private MyGallery                        gallery;
    private LinearLayout                     llDot;
    private RelativeLayout                   rlGoodsDetail;
    private Button                           btnAddToShoppingCar;
    private FrameLayout                      btnCollect;
    private Intent                           intent;
    public  ProductDetails.Product           product;
    private TextView                         tvGoodsDetailName;
    private TextView                         tvMarketPrice;
    private TextView                         tvMeberPrice;
    private EditText                         etCount;
    private RatingBar                        rbProductRating;
    private TextView                         tvStock;
    private TextView                         tvUserCommon;
    private Spinner                          spGoodsColor;
    private Spinner                          spGoodsSize;
    private int                              productCount;   //购买商品的数量
    private TextView                         tvDiscountMsg;
    private LinearLayout                     llColor;
    private LinearLayout                     llSize;
    private ArrayList<ProductDetails.Extras> extras;    //商品可选参数列名
    private HttpUtils                        mHttpUtils;
    private RequestParams                    params;
    private String                           uId;
    private String                           selectedColor;
    private String                           selectedSize;
    private String                           addShoppingCarUrl;
    private String                           addCollectionUrl;
    private Button                           btnShared;
    private String                           url;
    private ImageView                        ivFavorite;
    private TextView                         tvFavorite;
    private String                           deleteCollectionUrl;
    private LinearLayout                     llLoading;
    private GifView                          gf1;
    private LinearLayout                     llContent;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentItem = gallery.getSelectedItemPosition();
            int nextItem = currentItem + 1;
            gallery.setSelection(nextItem);
            sendEmptyMessageDelayed(0, 2000);
        }
    };
    private View markPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);

        tvCenter.setText("商品详情");
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText("前往购物车");

        Intent newIntent = getIntent();
        proId = newIntent.getStringExtra("id");
        System.out.append(proId + "id");

        gallery = (MyGallery) findViewById(R.id.gallery);
        llDot = (LinearLayout) findViewById(R.id.ll_dot);
        rlGoodsDetail = (RelativeLayout) findViewById(R.id.rl_goods_detail);

        tvGoodsDetailName = (TextView) rlGoodsDetail.findViewById(R.id.tv_goods_detail_name); //商品名称
        tvMarketPrice = (TextView) rlGoodsDetail.findViewById(R.id.tv_market_price);    //市场价
        tvMeberPrice = (TextView) rlGoodsDetail.findViewById(R.id.tv_member_price);   //会员价
        etCount = (EditText) rlGoodsDetail.findViewById(R.id.et_count);  //数量
        llColor = (LinearLayout) findViewById(R.id.ll_color);
        llSize = (LinearLayout) findViewById(R.id.ll_size);
        spGoodsColor = (Spinner) rlGoodsDetail.findViewById(R.id.sp_goods_color); //商品颜色
        spGoodsSize = (Spinner) rlGoodsDetail.findViewById(R.id.sp_goods_size); //商品尺寸
        rbProductRating = (RatingBar) rlGoodsDetail.findViewById(R.id.rbProductRating); //商品评分
        tvStock = (TextView) findViewById(R.id.tv_stock);   //库存
        tvUserCommon = (TextView) findViewById(R.id.tv_user_common);    //用户评论
        tvDiscountMsg = (TextView) findViewById(R.id.tv_discountMsg);   //优惠信息

        btnAddToShoppingCar = (Button) findViewById(R.id.btn_add_to_shopping_car);
        btnCollect = (FrameLayout) findViewById(R.id.btn_collect);
        ivFavorite = (ImageView) findViewById(R.id.iv_favorite);
        tvFavorite = (TextView) findViewById(R.id.tv_favorite);
        btnShared = (Button) findViewById(R.id.btn_shared);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);

        // 从xml中得到GifView的句柄
        gf1 = (GifView) llLoading.findViewById(R.id.gif1);
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

        //触发handler
        mHandler.sendEmptyMessageDelayed(0, 2000);

        btnAddToShoppingCar.setOnClickListener(this);
        btnCollect.setOnClickListener(this);
        rlGoodsDetail.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnShared.setOnClickListener(this);

        initData();

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳到大图浏览界面
                intent = new Intent(GoodsDetailActivity.this, GoodsBigPictureScanActivity.class);
                intent.setAction("goodsDetailPictures");
                intent.putExtra("bigImgs", product.bigImgs);
                startActivity(intent);
            }
        });

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = position % product.smallImgs.size();
                int childCount = llDot.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ImageView ivDot = (ImageView) llDot.getChildAt(i);
                    ivDot.setEnabled(i == position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spGoodsColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //拿到被选择项的值
                selectedColor = (String) spGoodsColor.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGoodsSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //拿到被选择项的值
                selectedSize = (String) spGoodsSize.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void share() {
        //2、第三方分享-->把东西分享到第三方的社交平台  ShareSdk
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("我是分享的标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        //oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(url);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/beauty1.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);

    }

    public void initData() {
        //获取网络数据
        getDataFromServer();


    }

    public void getDataFromServer() {
        url = Globals.URL_PREFIX + GlobalConstants.PRODUCT_DETAIL + "?proId=" + proId;
        com.tmall.myredboy.utils.HttpUtils.getHttpDes(GoodsDetailActivity.this, url, ProductDetails.class, new com.tmall.myredboy.utils.HttpUtils.OnHttpSuccess() {
            @Override
            public void onSuccess(Object object) {

                llContent.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);

                ProductDetails productDetails = (ProductDetails) object;

                product = productDetails.product;
                gallery.setAdapter(new GalleryAdapter(GoodsDetailActivity.this));
                //设置数据
                tvGoodsDetailName.setText(product.name);    //商品名称
                System.out.println("product.marketprice" + product.marketprice);
                //                //判断是否有市场价
                //                if (TextUtils.isEmpty(product.marketprice) || product.marketprice == null) {
                //                    //让该控件隐藏
                //                    markPrice.setVisibility(View.GONE);
                //                } else {
                //                    markPrice.setVisibility(View.VISIBLE);
                //                    tvMarketPrice.setText("¥ " + product.marketprice); //市场价
                //                }
                tvMarketPrice.setText("¥ " + product.marketprice); //市场价
                tvMeberPrice.setText("¥ " + product.sellprice);    //会员价
                extras = product.extras;
                //判断是否有颜色和尺寸
                if (extras != null) {
                    initColorAndSize();
                }
                //判断是否有优惠信息
                if (!TextUtils.isEmpty(product.discountMsg)) {
                    tvDiscountMsg.setText(product.discountMsg);
                    tvDiscountMsg.setVisibility(View.VISIBLE);
                }
                //商品评分
                float score = Float.parseFloat(product.score);
                rbProductRating.setRating(score);
                tvStock.setText("查看库存:" + product.count);   //库存
                tvUserCommon.setText("用户评论: 共有" + product.commentCount + "人评论");    //评论
                createDot();

                ScanLogDao scanLogDao = ScanLogDao.getInstance(GoodsDetailActivity.this);
                boolean b = scanLogDao.addScanLog(GoodsDetailActivity.this, product.id);
                System.out.println("product.id = " + product.id);
                System.out.println("addScanLog = " + b);

            }
        });
    }

    //添加小圆点
    private void createDot() {
        //初始化显示位置
        int initPosition = Integer.MAX_VALUE / 2 % product.smallImgs.size();

        //初始化小圆点
        for (int i = 0; i < product.smallImgs.size(); i++) {
            ImageView ivDot = new ImageView(GoodsDetailActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.bottomMargin = 10;
            ivDot.setLayoutParams(params);
            ivDot.setImageResource(R.drawable.selector_dot);
            if (i == initPosition) {
                ivDot.setEnabled(true);
            } else {
                ivDot.setEnabled(false);
            }
            llDot.addView(ivDot);
        }
    }


    public void initColorAndSize() {
        if (extras.get(0) != null) {
            //设置颜色
            String colorSpName = product.extras.get(0).name;
            ArrayList<String> colorSpValue = product.extras.get(0).value;
            spGoodsColor.setPrompt(colorSpName);
            ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, colorSpValue);
            //设置下拉列表的风格
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spGoodsColor.setAdapter(adapter1);//将adapter 添加到spinner中
            llColor.setVisibility(View.VISIBLE);
        } else {
            llColor.setVisibility(View.GONE);
        }

        if (extras.size() > 1 && extras.get(1) != null) {
            //设置尺寸
            String sizeSpName = product.extras.get(1).name;
            ArrayList<String> sizeSpValue = product.extras.get(1).value;
            spGoodsSize.setPrompt(sizeSpName);
            ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sizeSpValue);
            //设置下拉列表的风格
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spGoodsSize.setAdapter(adapter2);//将adapter 添加到spinner中
            llSize.setVisibility(View.VISIBLE);
        } else {
            llSize.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.btn_shared:
                share();
                break;

            case R.id.btn_add_to_shopping_car:
                //将商品加入购物车的逻辑
                addProductToShoppingCar();
                break;
            case R.id.btnRight:
                //跳转购物车
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("data", 3);
                startActivity(intent);
                break;
            case R.id.btn_collect:
                if (!product.hasCollected) {
                    //将商品加入收藏的逻辑
                    addProductToCollection();
                } else {
                    //将收藏的置为未收藏的逻辑
                    deleteProductFromCollection();
                }
                break;
            case R.id.rl_goods_detail:
                //跳转到纯文本的商品详情页面
                intent = new Intent(GoodsDetailActivity.this, GoodsBigPictureScanActivity.class);
                intent.setAction("goodsDetailText");
                intent.putExtra("desc", product.desc);
                intent.putExtra("name", product.name);
                System.out.println("name = " + product.name);
                System.out.println("desc = " + product.desc);
                startActivity(intent);
                break;
            case R.id.rl_goods_stock:
                //跳到库存界面
                ToastUtils.show(GoodsDetailActivity.this, "跳到库存界面");
                break;
            case R.id.rl_goods_common:
                //跳到用户评论界面
                ToastUtils.show(GoodsDetailActivity.this, "跳到用户评论界面");
                break;
        }
    }

    //取消收藏
    private void deleteProductFromCollection() {
        //判断用户是否登录
        boolean isLogin = userIsLogin();
        if (!isLogin) {  //没有登录
            return;
        }
        deleteCollectionUrl = Globals.URL_PREFIX + GlobalConstants.PRODUCT_DELETE_COLLECTION;
        System.out.println("deleteCollectionUrl" + deleteCollectionUrl);
        params = new RequestParams();
        params.addBodyParameter("uId", uId);
        params.addBodyParameter("proId", proId);
        postHttp(deleteCollectionUrl, params);
    }

    //判断用户是否登录
    public boolean userIsLogin() {
        uId = PrefUtils.getString(GoodsDetailActivity.this, GlobalConstants.PREF_USER_ID, "");
        //uId=1537;
        //判断用户是否登录
        if ("".equals(uId)) {
            //提示用户是否需要登录
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您还没有登录,是否需要登录?");
            //builder.setIcon(); //设置图标
            builder.setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //跳到登录页面
                    Intent loginIntent = new Intent(GoodsDetailActivity.this, AccountActivity.class);
                    startActivity(loginIntent);
                }
            });

            builder.setNegativeButton("暂不登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return false;
        } else {
            return true; //用户已经登录
        }
    }

    //收藏商品
    public void addProductToCollection() {
        //判断商品id是否存在
        if (TextUtils.isEmpty(product.id)) {
            ToastUtils.show(GoodsDetailActivity.this, "该商品不存在");
            return;
        }
        //判断用户是否登录
        boolean isLogin = userIsLogin();
        if (!isLogin) {  //没有登录
            ToastUtils.show(GoodsDetailActivity.this, "您还未登录,收藏商品失败");
            return;
        }

        //判断是否收藏过该商品
        if (product.hasCollected) {
            ToastUtils.show(GoodsDetailActivity.this, "您已收藏过该商品");
            return;
        } else {
            addCollectionUrl = Globals.URL_PREFIX + GlobalConstants.PRODUCT_ADD_TO_COLLECTION;
            params = new RequestParams();
            params.addBodyParameter("uId", uId);
            params.addBodyParameter("proId", proId);
            postHttp(addCollectionUrl, params);
        }
    }

    //加入购物车
    public void addProductToShoppingCar() {
        //判断数量是否为空
        String str = etCount.getText().toString();
        if ("".equals(str)) {
            ToastUtils.show(GoodsDetailActivity.this, "请填写商品数量");
            return;
        } else {
            productCount = Integer.parseInt(str);  //数量
        }

        boolean isLogin = userIsLogin();
        if (!isLogin) {  //没有登录
            ToastUtils.show(GoodsDetailActivity.this, "您还未登录,商品添加失败");
            return;
        }
        //发送添加购物车的网络请求
        addShoppingCarUrl = Globals.URL_PREFIX + GlobalConstants.PRODUCT_ADD_TO_SHPPING_CAR;
        //请求参数
        params = new RequestParams();
        String extraMsg = "数量:" + productCount + "-选择颜色:" + selectedColor + "-选择大小:" + selectedSize;
        params.addBodyParameter("uId", uId + "");
        params.addBodyParameter("productId", product.id + "");
        params.addBodyParameter("extraMsg", extraMsg);
        postHttp(addShoppingCarUrl, params);
    }

    //一般的post请求
    public void postHttp(final String url, RequestParams params) {
        mHttpUtils = new HttpUtils();
        mHttpUtils.configTimeout(5000);
        HttpHandler<String> mHttpHandler;
        mHttpUtils.configCurrentHttpCacheExpiry(1);
        mHttpHandler = mHttpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                if (url.equals(addShoppingCarUrl)) {
                    EventBus.getDefault().post(new CartInfoUpdateEvent(true));
                    ToastUtils.show(GoodsDetailActivity.this, "添加购物车成功");
                } else if (url.equals(addCollectionUrl)) {
                    ToastUtils.show(GoodsDetailActivity.this, "收藏成功");
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qfav_misc_web_btn_favorite_pressed);
                    ivFavorite.setImageBitmap(bitmap);
                    tvFavorite.setText("已收藏");
                    product.hasCollected = true;
                } else if (url.equals(deleteCollectionUrl)) {
                    ToastUtils.show(GoodsDetailActivity.this, "取消收藏成功");
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qfav_misc_web_btn_favorite_nor);
                    ivFavorite.setImageBitmap(bitmap);
                    tvFavorite.setText("未收藏");
                    product.hasCollected = false;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                ToastUtils.show(GoodsDetailActivity.this, "亲，你的网络不给力哦！");
            }
        });
    }

    class GalleryAdapter extends BaseAdapter {
        private Context mContext;
        private int     count;

        public GalleryAdapter(Context context) {
            this.mContext = context;
            count = product.smallImgs.size();
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public String getItem(int i) {
            return product.smallImgs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = new ImageView(mContext);
            int pos = i % product.smallImgs.size();

            String smallImgsUrl = Globals.URL_IMAGE + product.smallImgs.get(pos);
            com.tmall.myredboy.utils.HttpUtils.viewSetImage(GoodsDetailActivity.this, smallImgsUrl, (ImageView) view);
            // 在此最好判断一下view是否为空
            if (view != null) {
                //                view.setAdjustViewBounds(true);
                view.setBackgroundResource(R.drawable.photo_background);
                // 设置宽高
                view.setLayoutParams(new Gallery.LayoutParams(300, 300));
            }
            return view;

        }
    }

}
