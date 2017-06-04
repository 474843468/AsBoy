package more_activity;

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
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText etemil;
    private TextView etpassword;
    private EditText confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
               // overridePendingTransition(R.anim.from_right_in,R.anim.to_right_out);

                overridePendingTransition(R.anim.from_right_in,R.anim.to_right_out);
            }
        });
        Button regisister = (Button) findViewById(R.id.btn_register);
        etemil = (EditText) findViewById(R.id.et_emil);
        etpassword = (TextView) findViewById(R.id.et_password);
        confirm = (EditText) findViewById(R.id.ed_confirm);
        regisister.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title_tv);
        title.setText("用户注册");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
                break;

            case R.id.btn_register:
               String  email= etemil.getText().toString().trim();
                String password =  etpassword.getText().toString().trim();
                String con=confirm.getText().toString().trim();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(con)){
                    ToastUtils.show(getApplicationContext(),"请填写完整信息!");
                    return;
                }
                //匹配正则

                HttpUtils utils = new HttpUtils();
                utils.configTimeout(5000);
                RequestParams requestParams = new RequestParams();
                requestParams.addBodyParameter("email",email);
                requestParams.addBodyParameter("password",password);
                requestParams.addBodyParameter("con",con);

                utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "customer/customer_register.html", requestParams,new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        parserJson(responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                }) ;


                //给服务起提交数据
                //吐司返回登录界面

                break;
        }
    }
    public void    parserJson(String result){
        Gson gson = new Gson();
       // gson.fromJson(result,);   //解析对象不对成功判断状态吗跳登录界面  t
        //
        //注册
    }
}
