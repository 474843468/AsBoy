package more_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.tmall.myredboy.bean.CartInfoUpdateEvent;
import com.tmall.myredboy.bean.LoadInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import static android.content.ContentValues.TAG;

public class AccountActivity extends Activity {

    private Button          register;
    private Button          back;
    private TextView        title;
    private Button          load;
    private EditText        edname;
    private EditText        edpassword;
    private String          password;
    private String          name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        edname = (EditText) findViewById(R.id.ed_name);
        edpassword = (EditText) findViewById(R.id.ed_password);

        register = (Button) findViewById(R.id.submit);
        register.setVisibility(View.VISIBLE);
        register.setText("注册");
        title = (TextView) findViewById(R.id.title_tv);
        title.setText("账户中心");
        load = (Button) findViewById(R.id.btn_load);

        back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  startActivity(new Intent(AccountActivity.this, AccountDetailActivity.class));
                finish();
                overridePendingTransition(R.anim.from_right_in,R.anim.to_left_out);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, RegisterActivity.class));
            }
        });


        load.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                name = edname.getText().toString().trim();
                password = edpassword.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    ToastUtils.show(getApplicationContext(), "用户名或密码不能为空");
                    return;
                }
                netWork();

            }
        });
    }

    private void netWork() {
        //2.请求网络获 比对
        HttpUtils utils = new HttpUtils();
        utils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("uname", name);
        requestParams.addBodyParameter("pwd", password);

        utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX +
                "customer/customer_userLogin.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("接收到的结果是:" + responseInfo.result);
                parserJson(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show(getApplicationContext(), "服务器忙!");
                e.printStackTrace();
                Log.i(TAG, "错误信息" + s);
            }
        });
    }

    public void parserJson(String result) {
        Gson gson = new Gson();
        LoadInfo loadInfo = gson.fromJson(result, LoadInfo.class);
        PrefUtils.putString(getApplicationContext(),GlobalConstants.DATA,result);
        if (loadInfo.status == 200) {
            ToastUtils.show(getApplicationContext(), "登录成功!");
               PrefUtils.putString(getApplicationContext(),GlobalConstants.DATA,result);
            Intent intent = new Intent();
            intent.putExtra("loadInfo", loadInfo);
            intent.setClass(AccountActivity.this, AccountDetailActivity.class);
            startActivity(intent);
                  finish();
            EventBus.getDefault().post(new CartInfoUpdateEvent(true));
            overridePendingTransition(R.anim.from_right_in,R.anim.to_left_out);
        } else {
            ToastUtils.show(getApplicationContext(), "用户名或密码错误");
        }
        ;


    }
}


