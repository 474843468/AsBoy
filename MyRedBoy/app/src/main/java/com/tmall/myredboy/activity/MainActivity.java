package com.tmall.myredboy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.CartInfoUpdateEvent;
import com.tmall.myredboy.fragment.FragmentFactory;
import com.tmall.myredboy.fragment.HomeFragment;
import com.tmall.myredboy.fragment.ShoppingCarFragment;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private String[] mTitles = new String[]{"首页", "搜索", "分类", "购物车", "更多"};

    private LinearLayout   llHome;
    private LinearLayout   llSearch;
    private LinearLayout   llCategory;
    private RelativeLayout rlShoppingCar;
    private LinearLayout   llMore;
    private TextView       tvTitle;
    private ImageView      ivLogo;
    private RelativeLayout rlMain;
    public  TextView       tv;
    private TextView       tvShoppingCarNum;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataFromServer();
        //订阅观察者
        EventBus.getDefault().register(this);

        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        tvShoppingCarNum = (TextView) findViewById(R.id.tvShoppingCarNum);

        // 初始化控件
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        llHome = (LinearLayout) findViewById(R.id.llHome);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llCategory = (LinearLayout) findViewById(R.id.llCategory);
        rlShoppingCar = (RelativeLayout) findViewById(R.id.rlShoppingCar);
        llMore = (LinearLayout) findViewById(R.id.llMore);
        // 设置点击事件
        llHome.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        llCategory.setOnClickListener(this);
        rlShoppingCar.setOnClickListener(this);
        llMore.setOnClickListener(this);
        // 默认设置首页的icon状态
        llHome.setSelected(true);
        setCurrentFragment(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.llHome:
                setCurrentFragment(0);
                updateBottomIcon(0);
                break;
            case R.id.llSearch:
                setCurrentFragment(1);
                updateBottomIcon(1);
                break;
            case R.id.llCategory:
                setCurrentFragment(2);
                updateBottomIcon(2);
                break;
            case R.id.rlShoppingCar:
                setCurrentFragment(3);
                updateBottomIcon(3);
                break;
            case R.id.llMore:
                setCurrentFragment(4);
                updateBottomIcon(4);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int data = intent.getIntExtra("data", 0);
        setCurrentFragment(data);
        updateBottomIcon(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isUpdate) {
            setCurrentFragment(3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == Activity.RESULT_OK) {
                    String text = data.getStringExtra("data");
                    HomeFragment fragment = (HomeFragment) FragmentFactory.getFragment(0);
                    fragment.setSearch(text);
                }
                break;
        }
    }

    //更新底部icon的状态
    private void updateBottomIcon(int position) {
        if (position == 0) {
            ivLogo.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.GONE);
        } else {
            ivLogo.setVisibility(View.GONE);
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(mTitles[position]);
        }
        if (position == 3) {
            rlMain.setVisibility(View.GONE);
        } else {
            rlMain.setVisibility(View.VISIBLE);
        }
        llHome.setSelected(position == 0 ? true : false);
        llSearch.setSelected(position == 1 ? true : false);
        llCategory.setSelected(position == 2 ? true : false);
        rlShoppingCar.setSelected(position == 3 ? true : false);
        llMore.setSelected(position == 4 ? true : false);
    }

    // 替换帧布局中的内容
    private void setCurrentFragment(int position) {
        Fragment fragment = FragmentFactory.getFragment(position);// 得到要显示的fragment
        FragmentManager fm = getSupportFragmentManager();
        if (position == 3) {
            fragment = new ShoppingCarFragment();
            isUpdate = true;
        } else {
            isUpdate = false;
        }

        FragmentTransaction transaction = fm.beginTransaction();// 通过FragmentManager得到fragment事务
        transaction.replace(R.id.flContent, fragment);// 将fragment放到content_home中进行显示
        transaction.commit();// 将事务进行提交
    }

    //更新购物车
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateNum(CartInfoUpdateEvent event) {
        if (event.success) {
            getDataFromServer();
        }
    }

    private void getDataFromServer() {
        String uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");
        if ("".equals(uId)) {
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configHttpCacheSize(0);
        String url = GlobalConstants.URL_PREFIX + GlobalConstants.SHOPPING_CAR + "?uId=" + uId;
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                try {
                    JSONObject jo = new JSONObject(json);
                    int totalCount = jo.getInt("totalCount");
                    tvShoppingCarNum.setText(String.valueOf(totalCount));
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvShoppingCarNum.setText(String.valueOf(0));
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                tvShoppingCarNum.setText(String.valueOf(0));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}