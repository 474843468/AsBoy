package com.tmall.myredboy.activity.zl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmall.myredboy.R;



public class TransportTimeActiviyt extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_transport_time);

	   intent = new Intent();

	   RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.rl1);
	   RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.rl2);
	   RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rl3);

	   final TextView tv1 = (TextView) findViewById(R.id.tv1);
	   final TextView tv2 = (TextView) findViewById(R.id.tv2);
	   final TextView tv3 = (TextView) findViewById(R.id.tv3);

	   final ImageView iv1 = (ImageView) findViewById(R.id.iv1);
	   final ImageView iv2 = (ImageView) findViewById(R.id.iv2);
	   final ImageView iv3 = (ImageView) findViewById(R.id.iv3);


	   rl1.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 iv1.setVisibility(View.VISIBLE);
			 iv2.setVisibility(View.GONE);
			 iv3.setVisibility(View.GONE);

			 intent.putExtra("msg", tv1.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rl2.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 iv2.setVisibility(View.VISIBLE);
			 iv1.setVisibility(View.GONE);
			 iv3.setVisibility(View.GONE);

			 intent.putExtra("msg", tv2.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rl3.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 iv3.setVisibility(View.VISIBLE);
			 iv1.setVisibility(View.GONE);
			 iv2.setVisibility(View.GONE);

			 intent.putExtra("msg", tv3.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });


    }

}