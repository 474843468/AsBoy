package com.tmall.myredboy.activity.zl;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class BillActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_bill);

	   intent = new Intent();
	   RelativeLayout rlDW = (RelativeLayout) findViewById(R.id.rlDW);
	   RelativeLayout rlGR = (RelativeLayout) findViewById(R.id.rlGR);

	   RelativeLayout rlTS = (RelativeLayout) findViewById(R.id.rlTS);
	   RelativeLayout rlYX = (RelativeLayout) findViewById(R.id.rlYX);
	   RelativeLayout rlGAME = (RelativeLayout) findViewById(R.id.rlGAME);
	   RelativeLayout rlRJ = (RelativeLayout) findViewById(R.id.rlRJ);
	   RelativeLayout rlZL = (RelativeLayout) findViewById(R.id.rlZL);

	   final TextView tvDW = (TextView) findViewById(R.id.tvDW);
	   final TextView tvGR = (TextView) findViewById(R.id.tvGR);
	   final TextView tvTS = (TextView) findViewById(R.id.tvTS);
	   final TextView tvYX = (TextView) findViewById(R.id.tvYX);
	   final TextView tvGAME = (TextView) findViewById(R.id.tvGAME);
	   final TextView tvRJ = (TextView) findViewById(R.id.tvRJ);
	   final TextView tvZL = (TextView) findViewById(R.id.tvZL);

	   final ImageView ivDW = (ImageView) findViewById(R.id.ivDW);
	   final ImageView ivGR = (ImageView) findViewById(R.id.ivGR);
	   final ImageView ivTS = (ImageView) findViewById(R.id.ivTS);
	   final ImageView ivYX = (ImageView) findViewById(R.id.ivYX);
	   final ImageView ivGAME = (ImageView) findViewById(R.id.ivGAME);
	   final ImageView ivRJ = (ImageView) findViewById(R.id.ivRJ);
	   final ImageView ivZL = (ImageView) findViewById(R.id.ivZL);


	   final StringBuffer buffer = new StringBuffer();
	   buffer.append("个人");

	   rlDW.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 buffer.delete(0,buffer.length());
			 ivDW.setVisibility(View.VISIBLE);
			 ivGR.setVisibility(View.GONE);
			 buffer.append(tvDW.getText());
		  }
	   });
	   rlGR.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 buffer.delete(0,buffer.length());
			 ivGR.setVisibility(View.VISIBLE);
			 ivDW.setVisibility(View.GONE);
			 buffer.append(tvGR.getText());
		  }
	   });


	   rlTS.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivTS.setVisibility(View.VISIBLE);
			 ivYX.setVisibility(View.GONE);
			 ivGAME.setVisibility(View.GONE);
			 ivRJ.setVisibility(View.GONE);
			 ivZL.setVisibility(View.GONE);

			 intent.putExtra("msg", buffer.toString()+"/"+tvTS.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlYX.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivTS.setVisibility(View.GONE);
			 ivYX.setVisibility(View.VISIBLE);
			 ivGAME.setVisibility(View.GONE);
			 ivRJ.setVisibility(View.GONE);
			 ivZL.setVisibility(View.GONE);

			 intent.putExtra("msg", buffer.toString()+"/"+tvYX.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlGAME.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivTS.setVisibility(View.GONE);
			 ivYX.setVisibility(View.GONE);
			 ivGAME.setVisibility(View.VISIBLE);
			 ivRJ.setVisibility(View.GONE);
			 ivZL.setVisibility(View.GONE);

			 intent.putExtra("msg", buffer.toString()+"/"+tvGAME.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlRJ.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivTS.setVisibility(View.GONE);
			 ivYX.setVisibility(View.GONE);
			 ivGAME.setVisibility(View.GONE);
			 ivRJ.setVisibility(View.VISIBLE);
			 ivZL.setVisibility(View.GONE);

			 intent.putExtra("msg", buffer.toString()+"/"+tvRJ.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });

	   rlZL.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View v) {
			 ivTS.setVisibility(View.GONE);
			 ivYX.setVisibility(View.GONE);
			 ivGAME.setVisibility(View.GONE);
			 ivRJ.setVisibility(View.GONE);
			 ivZL.setVisibility(View.VISIBLE);

			 intent.putExtra("msg", buffer.toString()+"/"+tvZL.getText());
			 setResult(RESULT_OK, intent);
			 finish();//结束当前Acitvity
		  }
	   });


    }
}
