package com.tmall.myredboy.activity.zl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class WayOfPayActivity extends AppCompatActivity {


    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_way_of_pay);


	   intent = new Intent();


	   final ImageView ivCash = (ImageView) findViewById(R.id.ivCash);
	   final ImageView ivLater = (ImageView) findViewById(R.id.ivLater);
	   final ImageView ivZFB = (ImageView) findViewById(R.id.ivZFB);

	   final TextView tvCash = (TextView) findViewById(R.id.tvCash);
	   final TextView tvLater = (TextView) findViewById(R.id.tvLater);
	   final TextView tvZFB = (TextView) findViewById(R.id.tvZFB);

	   RelativeLayout rlCash = (RelativeLayout) findViewById(R.id.rlCash);
	   RelativeLayout rlZFB = (RelativeLayout) findViewById(R.id.rlZFB);
	   RelativeLayout rlLater = (RelativeLayout) findViewById(R.id.rlLater);

	   rlCash.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			ivCash.setVisibility(View.VISIBLE);
			ivZFB.setVisibility(View.GONE);
			ivLater.setVisibility(View.GONE);

			 intent.putExtra("msg", tvCash.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlZFB.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivCash.setVisibility(View.GONE);
			 ivZFB.setVisibility(View.VISIBLE);
			 ivLater.setVisibility(View.GONE);

			 intent.putExtra("msg", tvZFB.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity

		  }
	   });

	   rlLater.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivCash.setVisibility(View.GONE);
			 ivZFB.setVisibility(View.GONE);
			 ivLater.setVisibility(View.VISIBLE);

			 intent.putExtra("msg", tvLater.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity

		  }
	   });
















    }


}
