package more_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.AddressManagerActivity;
import com.tmall.myredboy.activity.FavoritesActivity;
import com.tmall.myredboy.activity.MyOrderActivity;
import com.tmall.myredboy.bean.LoadInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;

import static com.tmall.myredboy.R.id.card;

public class AccountDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button          back;
    private LoadInfo loadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        back = (Button) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.title_tv);
        title.setText("账户中心");
        Button submit = (Button) findViewById(R.id.submit);
        submit.setVisibility(View.VISIBLE);
        submit.setText("退出登录");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountDetailActivity.this,AccountActivity.class));
                //overridePendingTransition(R.anim.from_right_in,R.anim.to_right_out);
                startActivity(new Intent(AccountDetailActivity.this,AccountActivity.class));
                PrefUtils.remove(getApplicationContext(),GlobalConstants.PREF_USER_ID);
                PrefUtils.remove(getApplicationContext(),GlobalConstants.DATA);
                          finish();
               overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
            }
        });

        Intent intent = getIntent();
        loadInfo = (LoadInfo) intent.getSerializableExtra("loadInfo");
        TextView uname = (TextView) findViewById(R.id.tvName);

        uname.setText(loadInfo.user.username);
        TextView count = (TextView) findViewById(R.id.tv_countNum);
        count.setText(String.valueOf(loadInfo.user.userScore));
        TextView dj = (TextView) findViewById(R.id.hydj);
        switch (loadInfo.user.memberLevel) {
            case 0:
                dj.setText("普通会员");
            break;

            case 1:
                dj.setText("银牌会员");
            break;
            case 2:
                dj.setText("金牌会员 ");
            break;
            case 3:
                dj.setText("钻石会员");
            break;
        }


        PrefUtils.putString(getApplicationContext(), GlobalConstants.PREF_USER_ID,String.valueOf(loadInfo.user.id));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
            }
        });
        //地址管理
        TextView adressManager = (TextView) findViewById(R.id.tv_adressManager);
        adressManager.setText("地址管理");
        adressManager .setOnClickListener(this);

        TextView myForm = (TextView) findViewById(R.id.tv_myForm);
        myForm.setText("我的订单("+ loadInfo.user.orderCount +")");
        myForm.setOnClickListener(this);

        TextView card = (TextView) findViewById(R.id.card);
        card.setText("优惠券/礼品卡(0)");
        card .setOnClickListener(this);

        TextView stow = (TextView) findViewById(R.id.stow);
        stow.setText("收藏夹("+loadInfo.user.collectionCount+")");
        stow .setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //地址管理
            case R.id.tv_adressManager:
                startActivity(new Intent(this, AddressManagerActivity.class));
                break;
            //我的订单
            case R.id.tv_myForm:
                startActivity(new Intent(this, MyOrderActivity.class));
                break;
            //礼品卡
            case card:
                break;
            //收藏夹
            case R.id.stow:
                startActivity(new Intent(this, FavoritesActivity.class));
                break;
        }
    }

}
