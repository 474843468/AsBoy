package com.tmall.myredboy.activity.zl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.MainActivity;
import com.tmall.myredboy.activity.MyOrderActivity;

public class SuccessActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_success);

	   TextView tv = (TextView) findViewById(R.id.Result);
	   Intent intent = getIntent();
	   String id = intent.getStringExtra("id");
	   String way = intent.getStringExtra("way");
	   String price = intent.getStringExtra("price");
	   //"您的订单号 : 18383838438\n应付金额 : ￥123\n支付方式 :　到付－ＰＯＳ"
	   tv.setText("您的订单号 : "+id+"\n应付金额 : ￥"+price+"\n支付方式 :"+way);

	   TextView tvResult = (TextView) findViewById(R.id.tvResult);
	   Button btnShopping = (Button) findViewById(R.id.GoonShpping);
	   Button btnCheck = (Button) findViewById(R.id.checkList);

	   btnCheck.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
		  	startActivity(new Intent(SuccessActivity.this, MyOrderActivity.class));
		  }
	   });

	   btnShopping.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 startActivity(new Intent(SuccessActivity.this, MainActivity.class));
		  }
	   });
	   //获取数据给result设置数据
    }

    @Override
    public void onBackPressed() {
	   super.onBackPressed();
	   startActivity(new Intent(SuccessActivity.this, MainActivity.class));
    }
}
