package com.tmall.myredboy.activity.lqq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.BaseActivity;
import com.tmall.myredboy.global.Globals;
import com.tmall.myredboy.utils.HttpUtils;

import java.util.ArrayList;


public class GoodsBigPictureScanActivity extends BaseActivity {

    private ViewPager viewPager;
    private LinearLayout llDot;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mHandler.removeCallbacksAndMessages(null);
            int currentItem = viewPager.getCurrentItem();
            int nextItem = currentItem + 1;
            viewPager.setCurrentItem(nextItem);
            sendEmptyMessageDelayed(0, 2000);
        }
    };
    private String goodsDetailText;
    private ScrollView rlGoodsDetailText;
    private RelativeLayout rlGoodsDetailPictur;
    private ArrayList<String> bigImgs;
    private TextView tvGoodsName;
    private TextView tvGoodsDesc;
    private String goodsName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_big_picture_scan);

        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        rlGoodsDetailText = (ScrollView) findViewById(R.id.rl_goods_detail_text);
        rlGoodsDetailPictur = (RelativeLayout) findViewById(R.id.rl_goods_detail_picture);
        tvGoodsName = (TextView) rlGoodsDetailText.findViewById(R.id.tv_goods_name);
        tvGoodsDesc = (TextView) rlGoodsDetailText.findViewById(R.id.tv_goods_desc);

        intent = getIntent();
        String action = intent.getAction();

        //判断要显示大图浏览界面还是文本描述界面
        if (action.equals("goodsDetailText")) {
            rlGoodsDetailText.setVisibility(View.VISIBLE);
            rlGoodsDetailPictur.setVisibility(View.GONE);
            tvCenter.setText("商品详情");
            initTextData();

            return;

        } else {
            tvCenter.setText("大图浏览");
            bigImgs = intent.getStringArrayListExtra("bigImgs");
            rlGoodsDetailText.setVisibility(View.GONE);
            rlGoodsDetailPictur.setVisibility(View.VISIBLE);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        llDot = (LinearLayout) findViewById(R.id.ll_dot);

        //获取网络数据

        //给viewPager设置适配器
        viewPager.setAdapter(new MyViewPagerAdapter());

        //触发handler
        mHandler.sendEmptyMessageDelayed(0,2000);

        //初始化显示位置
        int initPosition = Integer.MAX_VALUE / 2 % bigImgs.size();
        while(initPosition!=0){
            initPosition--;
        }

        //初始化小圆点
        for (int i = 0; i < bigImgs.size(); i++) {
            ImageView ivDot = new ImageView(GoodsBigPictureScanActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
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

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
               switch (action) {
                   case MotionEvent.ACTION_DOWN:
                       mHandler.removeCallbacksAndMessages(null);
                       break;
                   case MotionEvent.ACTION_MOVE:
                       break;
                   case MotionEvent.ACTION_UP:
                       mHandler.sendEmptyMessageDelayed(0, 2000);
                       break;
               }
                return false;
            }
        });

        //给ViewPager添加监听器
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                position = position % bigImgs.size();
                int childCount = llDot.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    ImageView ivDot = (ImageView) llDot.getChildAt(i);
                    ivDot.setEnabled(i == position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void initTextData() {
        goodsName = intent.getStringExtra("name");
        tvGoodsName.setText(goodsName);
        goodsDetailText = intent.getStringExtra("desc");
        
        String newDesc = "";
        String[] split = goodsDetailText.split("\\n");
        for(int i = 0; i < split.length; i++) {
            String trim = split[i].trim();
            newDesc += (trim + "\n\n");
        }

        tvGoodsDesc.setText(newDesc);
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(GoodsBigPictureScanActivity.this, R.layout.layout_goods_big_picture, null);
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_image);
            position = position % bigImgs.size();
            String imageUrl = Globals.URL_IMAGE + bigImgs.get(position);
            HttpUtils.viewSetImage(GoodsBigPictureScanActivity.this, imageUrl, ivImage);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
