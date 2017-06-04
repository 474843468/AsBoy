package com.tmall.myredboy.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.wyy.LimitTimeActivity;
import com.tmall.myredboy.activity.wyy.NewsProductsActivity;
import com.tmall.myredboy.activity.wyy.PromoteActivity;
import com.tmall.myredboy.activity.wyy.RecommendActivity;
import com.tmall.myredboy.activity.wyy.SearchResultActivity;
import com.tmall.myredboy.activity.wyy.StarActivity;
import com.tmall.myredboy.bean.HotProductsInfo;
import com.tmall.myredboy.bean.VoiceInfo;
import com.tmall.myredboy.bean.wyy.HomeViewPagerInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.photo.CaptureActivity;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.tmall.myredboy.R.id.ll_dot;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private String[] mTitle = new String[]{"限时抢购", "促销快报", "新品上架", "热门单品", "品牌推荐"};
    private int[]    mIcon  = new int[]{R.drawable.selector_home_limittime, R.drawable.selector_home_promote, R.drawable.selector_home_newsproduct,
            R.drawable.selector_home_star, R.drawable.selector_home_recommed};

    private int initPosition;

    private ViewPager          viewPager;
    private ListView           lvList;
    private LinearLayout       llDot;
    private View               headerView;
    private HomeViewPagerInfo  homeViewPagerInfo;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageView[]        dot;
    public String tempStr = "";
    private ImageView searchIvDelete;
    private String    searchHistory;

    private List<HomeViewPagerInfo.HomeImages> imagesList;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mHandler.removeCallbacksAndMessages(null);
            int currentItem = viewPager.getCurrentItem();
            int nextItem = currentItem + 1;
            viewPager.setCurrentItem(nextItem);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    };
    private AutoCompleteTextView etContent;
    private Button               btnSearchKeyword;
    private ImageView            searchIvVoice;
    private ArrayList<String>    searchList;

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.fragment_home, null);

        //搜索框
        etContent = (AutoCompleteTextView) view.findViewById(R.id.search_et_input);
        //搜索
        btnSearchKeyword = (Button) view.findViewById(R.id.btn_search_keywords);
        //删除输入
        searchIvDelete = (ImageView) view.findViewById(R.id.search_iv_delete);

        searchIvVoice = (ImageView) view.findViewById(R.id.search_iv_voice);

        //语音按钮
        SpeechUtility.createUtility(activity, SpeechConstant.APPID + "=57dceca5");
        view.findViewById(R.id.search_iv_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startListen();
            }
        });
        //二维码按钮
        view.findViewById(R.id.search_iv_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
            }
        });

        headerView = View.inflate(activity, R.layout.layout_home_list_header, null);

        viewPager = (ViewPager) headerView.findViewById(R.id.viewPager);
        llDot = (LinearLayout) headerView.findViewById(ll_dot);

        lvList = (ListView) view.findViewById(R.id.lv_list);

        lvList.addHeaderView(headerView);

        lvList.setAdapter(new MyLvAdapter());

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position = position - lvList.getHeaderViewsCount();
                switch (position) {
                    case 0:
                        Intent intent = new Intent(activity, LimitTimeActivity.class);
                        activity.startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(activity, PromoteActivity.class);
                        activity.startActivity(intent1);
                        break;


                    case 2:
                        Intent intent2 = new Intent(activity, NewsProductsActivity.class);
                        activity.startActivity(intent2);
                        break;


                    case 3:
                        Intent intent3 = new Intent(activity, StarActivity.class);
                        activity.startActivity(intent3);
                        break;

                    case 4:
                        Intent intent4 = new Intent(activity, RecommendActivity.class);
                        activity.startActivity(intent4);
                        break;
                }

            }
        });

        btnSearchKeyword.setOnClickListener(this);
        searchIvDelete.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();

        //热门搜索商品列表,获取商品name
        getSearchListFromServer();

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString())) {
                    searchIvDelete.setVisibility(View.VISIBLE);
                    searchIvVoice.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //搜索商品列表,获取商品名字,显示在搜索框下拉列表
    private void getSearchListFromServer() {
        OkHttpUtils.post().url(GlobalConstants.URL_PREFIX + "product/product_search.html").build().
                execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        System.out.println("网络异常...");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        HotProductsInfo hotProductsInfo = gson.fromJson(s, HotProductsInfo.class);
                        List<HotProductsInfo.ProductsBean> productList = hotProductsInfo.product;
                        searchList = new ArrayList<>();
                        for (int k = 0; k < productList.size(); k++) {
                            String name = productList.get(k).name;
                            searchList.add(name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                                R.layout.simple_dropdown_item_1line, searchList);

                        etContent.setAdapter(adapter);
                    }
                });
    }

    //跳转二维码扫描
    private void startScan() {
        activity.startActivityForResult(new Intent(activity, CaptureActivity.class), 10);
    }

    //数据返回
    public void setSearch(String text) {
        etContent.setText(text);
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_findSellProductImg.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
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
        homeViewPagerInfo = gson.fromJson(json, HomeViewPagerInfo.class);

        imagesList = homeViewPagerInfo.home_images;

        //初始化显示位置
        initPosition = Integer.MAX_VALUE / 2 % imagesList.size();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        mHandler.sendEmptyMessageDelayed(0, 2000);//触发第一个消息

        int len = imagesList.size();
        dot = new ImageView[len];
        for (int i = 0; i < len; i++) {
            dot[i] = new ImageView(activity);
            if (i == initPosition) {
                dot[i].setBackgroundResource(R.drawable.dot_red);
            } else {
                dot[i].setBackgroundResource(R.drawable.dot_white);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.bottomMargin = 8;
            dot[i].setLayoutParams(params);
            //添加到viewpager底部的线性布局里面
            llDot.addView(dot[i]);
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                position = position % imagesList.size();
                for (int i = 0; i < imagesList.size(); i++) {
                    dot[i].setBackgroundResource(R.drawable.dot_red);

                    if (position != i) {
                        dot[i].setBackgroundResource(R.drawable.dot_white);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_keywords:
                //保存
                String keyword = etContent.getText().toString().trim();
                if (!"".equals(searchHistory)) {
                    searchHistory = searchHistory + "," + keyword;
                } else {
                    searchHistory = keyword;
                }

                PrefUtils.putString(activity, "keyword", searchHistory);

                //根据关键字点击搜索按钮
                Intent intent = new Intent(activity, SearchResultActivity.class);
                activity.startActivity(intent);
                break;

            case R.id.search_iv_delete:
                //清空历史搜索
                etContent.setText("");
                searchIvDelete.setVisibility(View.GONE);
                searchIvVoice.setVisibility(View.VISIBLE);
                break;
        }


    }

    class MyViewPagerAdapter extends PagerAdapter {

        private BitmapUtils bitmapUtils;

        public MyViewPagerAdapter() {
            bitmapUtils = new BitmapUtils(activity);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE / 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % imagesList.size();
            ImageView iv = new ImageView(activity);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(activity, "点击了图片...");
                }
            });
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            bitmapUtils.display(iv, GlobalConstants.URL_IMAGE + homeViewPagerInfo.home_images.get(position).path);
            container.addView(iv);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onStart() {
        mHandler.sendEmptyMessageDelayed(0, 2000);
        super.onStart();
    }

    @Override
    public void onStop() {
        mHandler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    class MyLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
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
                convertView = View.inflate(activity, R.layout.home_item_list, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ivIcon.setImageResource(mIcon[position]);
            holder.tvTitle.setText(mTitle[position]);

            return convertView;
        }


    }

    static class ViewHolder {
        public ImageView ivIcon;
        public TextView  tvTitle;
    }

    private void startListen() {
        // 1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(activity, null);
        // 2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 3.设置回调接口
        mDialog.setListener(new RecognizerDialogListener() {

            @Override
            public void onResult(RecognizerResult result, boolean isLast) {
                String resultString = result.getResultString();
                System.out.println("resultString=" + resultString + ",isLast="
                        + isLast);
                // 显示识别出来的结果
                Gson gson = new Gson();
                VoiceInfo voiceInfo = gson.fromJson(resultString,
                        VoiceInfo.class);
                ArrayList<VoiceInfo.WSInfo> wsList = voiceInfo.ws;
                for (int i = 0; i < wsList.size(); i++) {
                    VoiceInfo.WSInfo wsInfo = wsList.get(i);
                    ArrayList<VoiceInfo.CWInfo> cwList = wsInfo.cw;
                    for (int j = 0; j < cwList.size(); j++) {
                        VoiceInfo.CWInfo cwInfo = cwList.get(j);
                        String w = cwInfo.w;
                        tempStr += w;
                    }
                }
                if (isLast) {// 此时代表识别结束
                    //String text = tempStr.replaceAll("\\pP|\\pS", "");
                    tempStr = tempStr.replaceAll("[，。！？~#￥%……&*（）—+“”《》、]", "");
                    etContent.setText(tempStr);
                    tempStr = "";
                }
            }

            @Override
            public void onError(SpeechError e) {
                String errorDescription = e.getErrorDescription();
                Log.e("VoiceListener", errorDescription);
            }
        });
        // 4.显示dialog，接收语音输入
        mDialog.show();
    }
}



