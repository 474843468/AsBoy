package com.tmall.myredboy.fragment;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.wyy.SearchResultActivity;
import com.tmall.myredboy.bean.HotProductsInfo;
import com.tmall.myredboy.bean.VoiceInfo;
import com.tmall.myredboy.bean.wyy.HotSearchInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.photo.CaptureActivity;
import com.tmall.myredboy.utils.PrefUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static android.view.View.VISIBLE;


public class SearchFragment extends BaseFragment implements View.OnClickListener {

    private AutoCompleteTextView searchEtInput;
    private Button               btnSearchKeyword;
    private LinearLayout         llLoad;
    private ImageView            ivHotSearch;
    private ImageView            ivHistorySearch;
    private ListView             lvHotSearch;
    private ListView             lvHistorySearch;
    private Button               btnDeleteHistory;
    private List<String>         hotContentList;
    private ListView             searchLvTips;
    private RelativeLayout       rlHotSearch;
    private RelativeLayout       rlHistorySearch;
    private List<String>         keywordsList;

    private HistorySearchAdapter historySearchAdapter;
    private String               searchHistory;

    private ImageView searchIvDelete;//删除键
    private ImageView searchIvVoice;  //语音

    public String tempStr = "";
    private ImageView         searchIvScan;
    private ArrayList<String> searchList;

    @Override
    public View initView() {

        View view = View.inflate(this.activity, R.layout.fragment_search, null);

        //搜索框
        searchEtInput = (AutoCompleteTextView) view.findViewById(R.id.search_et_input);
        //搜索框的下拉列表
        searchLvTips = (ListView) view.findViewById(R.id.search_lv_tips);
        //搜索关键字按钮
        btnSearchKeyword = (Button) view.findViewById(R.id.btn_search_keywords);

        rlHotSearch = (RelativeLayout) view.findViewById(R.id.rl_hot_search);
        rlHistorySearch = (RelativeLayout) view.findViewById(R.id.rl_history_search);

        ivHotSearch = (ImageView) view.findViewById(R.id.iv_hot_search);
        ivHistorySearch = (ImageView) view.findViewById(R.id.iv_history_search);

        lvHotSearch = (ListView) view.findViewById(R.id.lv_hot_search);
        lvHistorySearch = (ListView) view.findViewById(R.id.lv_history_search);

        llLoad = (LinearLayout) view.findViewById(R.id.ll_load);
        llLoad.setVisibility(View.VISIBLE);
        //清除历史搜索
        btnDeleteHistory = (Button) view.findViewById(R.id.btn_delete_history);

        searchLvTips = (ListView) view.findViewById(R.id.search_lv_tips);

        //删除输入
        searchIvDelete = (ImageView) view.findViewById(R.id.search_iv_delete);
        //语音
        searchIvVoice = (ImageView) view.findViewById(R.id.search_iv_voice);
        //二维码
        searchIvScan = (ImageView) view.findViewById(R.id.search_iv_scan);

        rlHotSearch.setOnClickListener(this);
        rlHistorySearch.setOnClickListener(this);

        btnDeleteHistory.setOnClickListener(this);
        btnSearchKeyword.setOnClickListener(this);

        searchIvDelete.setOnClickListener(this);
        //语音搜索
        SpeechUtility.createUtility(activity, SpeechConstant.APPID + "=57dceca5");
        searchIvVoice.setOnClickListener(this);
        //二维码扫描
        searchIvScan.setOnClickListener(this);

        return view;
    }

    //Activity创建后访问网络
    @Override
    public void initData() {
        // 热门搜索数据
        getSearchHotDataFromServer();

        //热门搜索商品列表,获取商品name
        getSearchListFromServer();

        //存储关键字的集合
        keywordsList = new ArrayList<>();
        searchHistory = PrefUtils.getString(activity, "keyword", "");

        if (!"".equals(searchHistory)) {
            String[] keywords = searchHistory.split(",");
            for (int i = 0; i < keywords.length; i++) {
                keywordsList.add(keywords[i]);
            }
        }
        historySearchAdapter = new HistorySearchAdapter();
        lvHistorySearch.setAdapter(historySearchAdapter);
        lvHistorySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchEtInput.setText(keywordsList.get(position));
                rlHistorySearch.setVisibility(VISIBLE);
            }
        });

        searchEtInput.addTextChangedListener(new TextWatcher() {
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

    private boolean isHot     = true;
    private boolean isHistory = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_et_input:
                searchIvVoice.setVisibility(View.GONE);
                break;
            //管理Listview显示以及隐藏
            case R.id.rl_hot_search:
                if (isHot) {
                    lvHotSearch.setVisibility(View.GONE);
                    ivHotSearch.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_arrow_down));
                    isHot = false;
                } else {
                    lvHotSearch.setVisibility(View.VISIBLE);
                    ivHotSearch.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_arrow_up));
                    isHot = true;
                }

                break;

            case R.id.rl_history_search:
                if (isHistory) {
                    lvHistorySearch.setVisibility(View.GONE);
                    ivHistorySearch.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_arrow_down));
                    isHistory = false;
                } else {
                    lvHistorySearch.setVisibility(View.VISIBLE);
                    ivHistorySearch.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_arrow_up));
                    isHistory = true;
                }
                break;

            case R.id.btn_search_keywords:
                //保存
                String keyword = searchEtInput.getText().toString().trim();
                if (!"".equals(searchHistory)) {
                    searchHistory = searchHistory + "," + keyword;
                } else {
                    searchHistory = keyword;
                }

                PrefUtils.putString(activity, "keyword", searchHistory);
                keywordsList.add(keyword);
                historySearchAdapter.notifyDataSetChanged();

                //根据关键字点击搜索按钮
                Intent intent = new Intent(activity, SearchResultActivity.class);
                intent.putExtra("data",keyword);
                activity.startActivity(intent);
                break;

            case R.id.btn_delete_history:
                //清空历史搜索
                keywordsList.clear();
                PrefUtils.putString(activity, "keyword", "");
                historySearchAdapter.notifyDataSetChanged();
                break;

            case R.id.search_iv_delete:
                //清空历史搜索
                searchEtInput.setText("");
                searchIvDelete.setVisibility(View.GONE);
                searchIvVoice.setVisibility(View.VISIBLE);
                break;

            case R.id.search_iv_voice:
                startListen();
                break;

            case R.id.search_iv_scan:
                startScan();
                break;
        }
    }

    //跳转二维码扫描
    private void startScan() {
        activity.startActivityForResult(new Intent(activity, CaptureActivity.class), 10);
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
                    searchEtInput.setText(tempStr);
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

    //历史搜索设置适配器
    class HistorySearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return keywordsList.size();
        }

        @Override
        public Object getItem(int position) {
            return keywordsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoryViewHolder holder;
            if (convertView == null) {
                holder = new HistoryViewHolder();
                convertView = View.inflate(activity, R.layout.search_history_item, null);
                holder.tvHistory = (TextView) convertView.findViewById(R.id.tv_history);
                convertView.setTag(holder);
            } else {
                holder = (HistoryViewHolder) convertView.getTag();
            }

            holder.tvHistory.setText(keywordsList.get(position));

            return convertView;
        }
    }

    static class HistoryViewHolder {
        public TextView tvHistory;
    }

    // 热门搜索数据
    private void getSearchHotDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.configSoTimeout(5000);
        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "product/product_findHotSearch.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                //热门搜索
                parseHotDataJson(json);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }
        });
    }


    private void parseHotDataJson(String json) {
        Gson gson = new Gson();
        HotSearchInfo hotSearchInfo = gson.fromJson(json, HotSearchInfo.class);

        hotContentList = hotSearchInfo.hotContent;
        llLoad.setVisibility(View.GONE);

        HotSearchAdapter hotSearchAdapter = new HotSearchAdapter();
        lvHotSearch.setAdapter(hotSearchAdapter);

        lvHotSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchEtInput.setText(hotContentList.get(position));
            }
        });
    }

    class HotSearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return hotContentList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotContentList.get(position);
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
                convertView = View.inflate(activity, R.layout.search_fragment_hot_title, null);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(hotContentList.get(position));
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
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

                        searchEtInput.setAdapter(adapter);
                    }
                });
    }

}
