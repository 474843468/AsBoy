package com.tmall.myredboy.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.TransInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.List;

public class TranslationActivity extends BaseActivity {

    private String   oId;
    private TextView tvTransStyle;
    private TextView tvDelivery;
    private TextView tvCompany;
    private TextView tvNum;
    private TextView tvDeliveryInfo;
    private TextView tvDeliveryDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_info);
        tvCenter.setText("物流查询");
        oId = getIntent().getStringExtra("data");

        tvTransStyle = (TextView) findViewById(R.id.tvTransStyle);
        tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvNum = (TextView) findViewById(R.id.tvNum);
        tvDeliveryInfo = (TextView) findViewById(R.id.tvDeliveryInfo);
        tvDeliveryDetail = (TextView) findViewById(R.id.tvDeliveryDetail);

        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("oId", oId);
        httpUtils.send(HttpRequest.HttpMethod.POST,
                GlobalConstants.URL_PREFIX + "product/order_findDelivery.html",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parseJson(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        ToastUtils.show(TranslationActivity.this, "网络正忙，请稍候...");
                    }
                });
    }

    private void parseJson(String json) {
        Gson gson = new Gson();
        TransInfo transInfo = gson.fromJson(json, TransInfo.class);
        TransInfo.ContentBean content = transInfo.content;

        tvTransStyle.setText(content.type);
        tvDelivery.setText(content.code);
        tvCompany.setText(content.company);
        tvNum.setText(content.code2);

        String deliveryInfo = "<font color='black'><b>物流跟踪：</b></font>" +
                "<font>以下信息由物流公司提供，如有疑问,请查询" + content.company + "官方网站</font>";
        //tv.setText(Html.fromHtml(newMessageInfo));
        tvDeliveryInfo.setText(Html.fromHtml(deliveryInfo));
        List<String> orderDetail = content.orderDetail;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < orderDetail.size(); i++) {
            sb.append(orderDetail.get(i));
            if (i != orderDetail.size() - 1) {
                sb.append("\n");
            }
        }
        tvDeliveryDetail.setText(sb.toString());
    }
}
