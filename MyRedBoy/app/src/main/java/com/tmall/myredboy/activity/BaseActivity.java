package com.tmall.myredboy.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class BaseActivity extends FragmentActivity implements View.OnClickListener {

    public TextView  tvCenter;
    public ImageView ivCenter;
    public Button    btnLeft;
    public Button    btnRight;

    @Override
    public void setContentView(int layoutResID) {
        // 得到包含了title的布局
        View baseView = getLayoutInflater().inflate(R.layout.activity_base, null);
        // 得到存放内容的控件
        FrameLayout flContent = (FrameLayout) baseView
                .findViewById(R.id.flContentBase);
        // 加载子类setContentView中传递进来的布局文件
        View childView = getLayoutInflater().inflate(layoutResID, null);
        // 将子类需要显示的内容放到flContent中进行显示
        flContent.addView(childView);

        // 找出title布局中的控件
        tvCenter = (TextView) baseView.findViewById(R.id.tvCenter);
        ivCenter = (ImageView) baseView.findViewById(R.id.ivCenter);

        btnLeft = (Button) baseView.findViewById(R.id.btnLeft);
        btnRight = (Button) baseView.findViewById(R.id.btnRight);
        // 设置默认状态，默认状态一般根据最常见的状态为主
        ivCenter.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(this);
        btnLeft.setText("返回");

        super.setContentView(baseView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnLeft) {
            finish();
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.to_right_out);
    }

}
