package com.tmall.myredboy.activity.zl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class KDActivity extends AppCompatActivity {

    private Intent intent;

    /**
	* 快递界面
	*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_kd);

	   intent = new Intent();

	   RelativeLayout rlST = (RelativeLayout) findViewById(R.id.rlST);
	   RelativeLayout rlYD = (RelativeLayout) findViewById(R.id.rlYD);
	   RelativeLayout rlYT = (RelativeLayout) findViewById(R.id.rlYD);

	   final TextView tvST = (TextView) findViewById(R.id.tvST);
	   final TextView tvYD = (TextView) findViewById(R.id.tvYD);
	   final TextView tvYT = (TextView) findViewById(R.id.tvYT);

	   final ImageView ivST = (ImageView) findViewById(R.id.ivST);
	   final ImageView ivYD = (ImageView) findViewById(R.id.ivYD);
	   final ImageView ivYT = (ImageView) findViewById(R.id.ivYT);

	   rlST.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivST.setVisibility(View.VISIBLE);
			 ivYD.setVisibility(View.GONE);
			 ivYT.setVisibility(View.GONE);

			 intent.putExtra("msg", tvST.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlYD.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivST.setVisibility(View.GONE);
			 ivYD.setVisibility(View.VISIBLE);
			 ivYT.setVisibility(View.GONE);

			 intent.putExtra("msg", tvYD.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity


		  }
	   });

	   rlYT.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivST.setVisibility(View.GONE);
			 ivYD.setVisibility(View.GONE);
			 ivYT.setVisibility(View.VISIBLE);

			 intent.putExtra("msg", tvYT.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity

		  }
	   });

    }
}
