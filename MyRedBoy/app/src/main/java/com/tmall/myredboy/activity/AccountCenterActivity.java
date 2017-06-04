package com.tmall.myredboy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tmall.myredboy.R;

public class AccountCenterActivity extends Activity {

    private Button   btnBack;
    private Button   btnAccount;
    private ListView accListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	   super.onCreate(savedInstanceState);
	   setContentView(R.layout.activity_account_center);


	   btnBack = (Button) findViewById(R.id.btnBack);
	   btnAccount = (Button) findViewById(R.id.btnAccount);
	   accListView = (ListView) findViewById(R.id.accountListview);

	   //添加头布局脚布局


	   //初始化数据,设置适配器
	   inidate();
    }

    private void inidate() {
	   //加载头布局
	   View hewdView = View.inflate(AccountCenterActivity.this, R.layout.account_center_head, null);
	   //加载脚布局
	   View footView = View.inflate(AccountCenterActivity.this, R.layout.account_center_foot, null);
	   accListView.addFooterView(footView);
	   accListView.addHeaderView(hewdView);
	   //设置数据适配器
	   accListView.setAdapter(new MyAdapter());

    }

    class MyAdapter extends BaseAdapter {

	   @Override
	   public int getCount() {
		  return 1;
	   }

	   @Override
	   public Object getItem(int position) {
		  return null;
	   }

	   @Override
	   public long getItemId(int position) {
		  return position;
	   }

	   @Override
	   public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder holder1 = null;
		  if (convertView == null) {
			 holder1 = new ViewHolder();
			 convertView = View.inflate(AccountCenterActivity.this, R.layout.shoppingcar_listitem, null);
			 holder1.color = (TextView) convertView.findViewById(R.id.tvColor);
			 holder1.count = (TextView) convertView.findViewById(R.id.tvAll);
			 holder1.name = (TextView) convertView.findViewById(R.id.tvName);
			 holder1.num = (TextView) convertView.findViewById(R.id.tvNum);
			 holder1.size = (TextView) convertView.findViewById(R.id.tvSize);
			 holder1.picture = (ImageView) convertView.findViewById(R.id.ivImage);
			 holder1.delete = (ImageView) convertView.findViewById(R.id.ivDelete);
			 holder1.delete.setVisibility(View.GONE);
			 convertView.setTag(holder1);
		  } else {

			 holder1 = (ViewHolder) convertView.getTag();
		  }
		 return  convertView;
	   }
    }

    static class ViewHolder {
	   ImageView picture;
	   TextView  num;//数量
	   TextView  color;    //颜色
	   TextView  size;
	   TextView  count;
	   TextView  name;
	   ImageView delete;

    }
}
