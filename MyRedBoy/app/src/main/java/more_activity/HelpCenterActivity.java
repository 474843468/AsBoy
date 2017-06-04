package more_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.SenActivity;
import com.tmall.myredboy.bean.YangLingInfo;
import com.tmall.myredboy.global.GlobalConstants;

import java.util.List;

public class HelpCenterActivity extends AppCompatActivity {

    private List<YangLingInfo.HelpListBean> helplist;
    private ListView                        lvlist;
    private TextView                       title1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help_center);

        lvlist = (ListView) findViewById(R.id.llist);
        title1 = (TextView) findViewById(R.id.title_tv);
        Button  back = (Button) findViewById(R.id.btn_back);
        TextView  title = (TextView) findViewById(R.id.title_tv);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
            }
        });
        title.setText("帮助中心");


        title1.setText("帮助中心");
        Button btn = (Button) findViewById(R.id.btn_number);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "40088888888";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(5000);
        utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.URL_PREFIX + "index/index_getHelpList.html", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parserJson(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

        lvlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        String afterurl = helplist.get(0).URL;
                        Intent intent = new Intent(HelpCenterActivity.this, AfterServiceActivity.class);
                        intent.putExtra("url", afterurl);
                        startActivity(intent);
                        //  startActivity(new Intent(HelpCenterActivity.this, AfterServiceActivity.class));
                        break;

                    case 0:
                        String helperurl = helplist.get(1).URL;
                        Intent intent1 = new Intent(HelpCenterActivity.this, YoungLingActivity.class);
                        intent1.putExtra("url", helperurl);
                        startActivity(intent1);
                        //startActivity(new Intent(HelpCenterActivity.this, HelpCenterActivity.class));
                        break;
                    case 2:
                        String senurl = helplist.get(2).URL;
                        Intent intent2 = new Intent(HelpCenterActivity.this, SenActivity.class);
                        intent2.putExtra("url", senurl);
                        startActivity(intent2);
                        //startActivity(new Intent(HelpCenterActivity.this, SenActivity.class));
                        break;
                }
            }
        });
    }

    public void parserJson(String result) {
        Gson gson = new Gson();
        YangLingInfo yangLingInfo = gson.fromJson(result, YangLingInfo.class);
        if (200 == yangLingInfo.status) {
            helplist = yangLingInfo.helpList;
            lvlist.setAdapter(new MyAdapter());

        }

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return helplist.size();
        }

        @Override
        public Object getItem(int position) {
            return helplist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView= View.inflate(HelpCenterActivity.this, R.layout.item_title_helper, null);
                holder = new ViewHolder();
                holder.tv = (TextView) convertView.findViewById(R.id.titletv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(helplist.get(position).name);
            return convertView;
        }


    }
    static class ViewHolder {
        TextView tv;
    }
}

