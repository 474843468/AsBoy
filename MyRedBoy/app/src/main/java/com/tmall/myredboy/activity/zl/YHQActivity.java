package com.tmall.myredboy.activity.zl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class YHQActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_yhq);

	   intent = new Intent();
	   RelativeLayout rlSHENG = (RelativeLayout) findViewById(R.id.rlSHENG);
	   RelativeLayout rlJY = (RelativeLayout) findViewById(R.id.rlJY);
	   RelativeLayout rlGQ = (RelativeLayout) findViewById(R.id.rlGQ);

	   final TextView tvSHENG = (TextView) findViewById(R.id.tvSHENG);
	   final TextView tvJY = (TextView) findViewById(R.id.tvJY);
	   final TextView tvGQ = (TextView) findViewById(R.id.tvGQ);

	   final ImageView ivSHENG = (ImageView) findViewById(R.id.ivSHENG);
	   final ImageView ivJY = (ImageView) findViewById(R.id.ivJY);
	   final ImageView ivGQ = (ImageView) findViewById(R.id.ivGQ);


	   rlGQ.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivGQ.setVisibility(View.VISIBLE);
			 ivJY.setVisibility(View.GONE);
			 ivSHENG.setVisibility(View.GONE);

			 intent.putExtra("msg", tvGQ.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlJY.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivJY.setVisibility(View.VISIBLE);
			 ivGQ.setVisibility(View.GONE);
			 ivSHENG.setVisibility(View.GONE);

			 intent.putExtra("msg", tvJY.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity


		  }
	   });

	   rlSHENG.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivSHENG.setVisibility(View.VISIBLE);
			 ivJY.setVisibility(View.GONE);
			 ivGQ.setVisibility(View.GONE);

			 intent.putExtra("msg", tvSHENG.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity


		  }
	   });


    }
}
