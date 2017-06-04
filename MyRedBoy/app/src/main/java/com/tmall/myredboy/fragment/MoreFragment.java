package com.tmall.myredboy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.LoadInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;

import more_activity.AboutActivity;
import more_activity.AccountActivity;
import more_activity.AccountDetailActivity;
import more_activity.HelpCenterActivity;
import more_activity.LookLogActivity;
import more_activity.UserFeedBackActivity;

public class MoreFragment extends BaseFragment implements View.OnClickListener {

    private TextView payCenter;
    private TextView about;
    private TextView lookLog;
    private TextView helpCentert;
    private TextView userFeedBack;
    private Button   btn;

    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.activity_more, null);
        TextView payCenter = (TextView) view.findViewById(R.id.payCenter);
        lookLog = (TextView) view.findViewById(R.id.lookLog);
        helpCentert = (TextView) view.findViewById(R.id.helpCenter);
        userFeedBack = (TextView) view.findViewById(R.id.userFeedBack);
        about = (TextView) view.findViewById(R.id.about);
        btn = (Button) view.findViewById(R.id.btn);
        payCenter.setOnClickListener(this);
        lookLog.setOnClickListener(this);
        helpCentert.setOnClickListener(this);
        userFeedBack.setOnClickListener(this);
        about.setOnClickListener(this);

        btn.setOnClickListener(this);


        return view;
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payCenter:

                String load = PrefUtils.getString(getContext(), GlobalConstants.DATA, "null");
                Gson gson = new Gson();
                LoadInfo loadInfo = gson.fromJson(load, LoadInfo.class);

                if(load.equals("null")){
                    startActivity(new Intent(activity, AccountActivity.class));
                }else{
                    /*startActivity(new Intent(activity, AccountDetailActivity.class));*/
                    Intent intent = new Intent();
                    intent.putExtra("loadInfo", loadInfo);
                    intent.setClass(getContext(), AccountDetailActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.lookLog:
                startActivity(new Intent(activity, LookLogActivity.class));
                break;
            case R.id.helpCenter:
                startActivity(new Intent(activity, HelpCenterActivity.class));
                break;
            case R.id.userFeedBack:
                startActivity(new Intent(activity, UserFeedBackActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(activity, AboutActivity.class));
                break;
            case R.id.btn:
                String number = "40088888888";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
                break;
        }
    }
}
