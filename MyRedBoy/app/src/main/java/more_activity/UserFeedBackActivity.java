package more_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.FeedActivity;
import com.tmall.myredboy.bean.FeedBack;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

public class UserFeedBackActivity extends AppCompatActivity {

    private EditText suggestion;
    private EditText etnumber;
    private String   contact;
    private String   content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_back);
        TextView title = (TextView) findViewById(R.id.title_tv);
        title.setText("用户反馈");
        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submit = (Button) findViewById(R.id.submit);
        submit.setVisibility(View.VISIBLE);
        suggestion = (EditText) findViewById(R.id.et_suggestion);
        etnumber = (EditText) findViewById(R.id.et_number);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = suggestion.getText().toString().trim();

                contact = etnumber.getText().toString().trim();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(contact)) {
                    ToastUtils.show(getApplicationContext(), "请填写完整的意见或联系方式!");
                    return;
                }
                netWork(content, contact);
            }
        });

    }

    public void netWork(String content, String contact) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter(" content", content);
        requestParams.addBodyParameter("contact", contact);
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "index/index_feedBack.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parserJson(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
            }
        });

    }

    public void parserJson(String result) {
        Gson gson = new Gson();
        FeedBack feedBack = gson.fromJson(result, FeedBack.class);
        if (200 == feedBack.status) {

            startActivity(new Intent(UserFeedBackActivity.this, FeedActivity.class));
        } else {
            ToastUtils.show(UserFeedBackActivity.this, "请输入完整的信息!");
        }
    }

}
