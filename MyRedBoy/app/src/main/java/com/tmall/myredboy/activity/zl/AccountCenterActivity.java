package com.tmall.myredboy.activity.zl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.AddressManagerActivity;
import com.tmall.myredboy.bean.AddressInfo;
import com.tmall.myredboy.bean.CartInfoUpdateEvent;
import com.tmall.myredboy.bean.ShoppingCarBean;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.SnackbarUtil;
import com.tmall.myredboy.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AccountCenterActivity extends Activity {

    private Button          btnBack;
    private Button          btnAccount;
    private ListView        accListView;
    private ShoppingCarBean bean;
    private TextView        tvway;
    private TextView        tvtime;
    private TextView        tvinfomation;
    private TextView        tvquick;
    private TextView        tvbill;
    private TextView        tvYHQ;
    private double fee = 10;
    private TextView                       footcount1;
    private TextView                       oraginalPri1;
    private TextView                       transpotation1;
    private TextView                       yHprice1;
    private TextView                       payPri1;
    private List<ShoppingCarBean.CartBean> cart;
    private String                         uid;
    private String                         address;
    private String                         area;
    private String                         name;
    private String                         telphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_center);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnAccount = (Button) findViewById(R.id.btnAccount);
        accListView = (ListView) findViewById(R.id.accountListview);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(AccountCenterActivity.this, SuccessActivity.class));
                getToCheck();
            }
        });

        inidate();
    }


    private void inidate() {
        uid = PrefUtils.getString(getApplicationContext(), GlobalConstants.PREF_USER_ID, "");

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalConstants.URL_PREFIX + GlobalConstants.SHOPPING_CAR + "?uId=1395", new RequestCallBack<String>() {


            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Gson gson = new Gson();
                bean = gson.fromJson(json, ShoppingCarBean.class);
                //System.out.print(bean.cart);

                setdata();

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.print("网络访问失败");

            }
        });


    }

    private void setdata() {
        //加载脚布局
        View footView = View.inflate(AccountCenterActivity.this, R.layout.account_center_foot, null);
        footcount1 = (TextView) footView.findViewById(R.id.footCount1);
        oraginalPri1 = (TextView) footView.findViewById(R.id.oraginalPr1);
        transpotation1 = (TextView) footView.findViewById(R.id.transpotation1);
        yHprice1 = (TextView) footView.findViewById(R.id.YHprice1);
        payPri1 = (TextView) footView.findViewById(R.id.payPri1);
        Button btn = (Button) footView.findViewById(R.id.CommitList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToCheck();
            }
        });


        cart = bean.cart;
        footcount1.setText(bean.totalCount + "");
        oraginalPri1.setText(bean.totalPrice + "");
        footcount1.setText(bean.totalCount + "");
        transpotation1.setText(fee + "");
        //YHprice1.setText(bean.discountPrice+"");
        payPri1.setText(bean.totalPrice - bean.discountPrice + fee + "");


        //加载头布局
        View headView = View.inflate(AccountCenterActivity.this, R.layout.account_center_head, null);
        RelativeLayout infomation = (RelativeLayout) headView.findViewById(R.id.infomation);
        RelativeLayout way = (RelativeLayout) headView.findViewById(R.id.way);
        RelativeLayout time = (RelativeLayout) headView.findViewById(R.id.time);
        RelativeLayout quick = (RelativeLayout) headView.findViewById(R.id.quick);
        RelativeLayout bill = (RelativeLayout) headView.findViewById(R.id.bill);
        RelativeLayout YHQ = (RelativeLayout) headView.findViewById(R.id.YHQ);

        tvway = (TextView) headView.findViewById(R.id.tvway);
        tvtime = (TextView) headView.findViewById(R.id.tvtime);
        tvinfomation = (TextView) headView.findViewById(R.id.tvinfomation);
        tvquick = (TextView) headView.findViewById(R.id.tvquick);
        tvbill = (TextView) headView.findViewById(R.id.tvbill);
        tvYHQ = (TextView) headView.findViewById(R.id.tvYHQ);


        infomation.setOnClickListener(new View.OnClickListener() {
            @Override //收货人信息
            public void onClick(View v) {
                Intent intent = new Intent(AccountCenterActivity.this, AddressManagerActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 10);
            }
        });

        way.setOnClickListener(new View.OnClickListener() {
            @Override  //支付方式
            public void onClick(View v) {
                //startActivity(new Intent(AccountCenterActivity.this, WayOfPayActivity.class));
                Intent intent = new Intent(AccountCenterActivity.this, WayOfPayActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 20);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override //送货时间
            public void onClick(View v) {
                //startActivity(new Intent(AccountCenterActivity.this, TransportTimeActiviyt.class));
                Intent intent = new Intent(AccountCenterActivity.this, TransportTimeActiviyt.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 30);
            }
        });

        quick.setOnClickListener(new View.OnClickListener() {
            @Override   //快递
            public void onClick(View v) {
                Intent intent = new Intent(AccountCenterActivity.this, KDActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 40);

            }
        });

        bill.setOnClickListener(new View.OnClickListener() {
            @Override  //发票
            public void onClick(View v) {
                //startActivity(new Intent(AccountCenterActivity.this, BillActivity.class));
                Intent intent = new Intent(AccountCenterActivity.this, BillActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 50);
            }
        });

        YHQ.setOnClickListener(new View.OnClickListener() {
            @Override //优惠券
            public void onClick(View v) {
                //startActivity(new Intent(AccountCenterActivity.this, YHQActivity.class));
                Intent intent = new Intent(AccountCenterActivity.this, YHQActivity.class);
                intent.putExtra("isSelect", true);
                startActivityForResult(intent, 60);
            }
        });


        accListView.addFooterView(footView);
        accListView.addHeaderView(headView);
        //设置数据适配器
        accListView.setAdapter(new MyAdapter());


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cart.size();
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
                holder1.price = (TextView) convertView.findViewById(R.id.tvPrice);
                holder1.picture = (ImageView) convertView.findViewById(R.id.ivSPPitcure);
                holder1.delete = (ImageView) convertView.findViewById(R.id.ivDelete);
                holder1.delete.setVisibility(View.GONE);
                convertView.setTag(holder1);
            } else {

                holder1 = (ViewHolder) convertView.getTag();
            }

            ShoppingCarBean.CartBean cartBean = cart.get(position);
            //颜色
            //小计
            holder1.count.setText("小计: " + cartBean.productPrice * cartBean.amount + "");//现在的价格*数量
            //名称
            holder1.name.setText(cartBean.productName);
            //数量
            holder1.num.setText("数量: " + cartBean.amount);
            //holder1.size.setText();    尺码
            //单价
            holder1.price.setText(cartBean.productPrice + "");
            //图片
            String coverImg = cartBean.coverImg;

		  Glide.with(getApplicationContext()).load(GlobalConstants.URL_IMAGE+coverImg).into(holder1.picture);

            return convertView;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10:
                //信息
                if (resultCode == RESULT_OK) {
                    if (resultCode == RESULT_OK) {
                        AddressInfo.AddressBean data1 = (AddressInfo.AddressBean) data.getSerializableExtra("data");
                        //详细地址
                        address = data1.address;
                        //地区
                        area = data1.area;
                        name = data1.name;
                        telphone = data1.telphone;
                        //tvinfomation.setText("收货地址: \n" + name + "\n" + telphone + "\n"+ address + area );
                        String s1 = "<font ><b>收货地址:</b></font>";
                        tvinfomation.setText(Html.fromHtml(s1) + "\n" + name + "\n" + telphone + "\n" + address + area);
                    }
                }
                break;
            case 20:
                //支付方式
                if (resultCode == RESULT_OK) {
                    String s1 = "<font ><b>" + "支付方式:\n" + "</b></font>" + data.getStringExtra("msg");
                    //tvway.setText("支付方式:\n" + data.getStringExtra("msg"));
                    tvway.setText(Html.fromHtml(s1));
                }
                break;
            case 30:
                //送货时间
                if (resultCode == RESULT_OK) {
                    //tvtime.setText("送货时间: \n" + data.getStringExtra("msg"));
                    String s1 = "<font ><b>" + "送货时间: \n" + "</b></font>" + data.getStringExtra("msg");
                    tvtime.setText(Html.fromHtml(s1));
                }
                break;
            case 40:
                //快递
                if (resultCode == RESULT_OK) {
                    //tvquick.setText("快递: " + data.getStringExtra("msg"));
                    String s1 = "<font ><b>" + "快递: " + "</b></font>" + data.getStringExtra("msg");
                    tvquick.setText(Html.fromHtml(s1));

                }
                break;
            case 50:
                if (resultCode == RESULT_OK) {
                    //tvbill.setText("发票: " + data.getStringExtra("msg"));
                    String s1 = "<font ><b>" + "发票: " + "</b></font>" + data.getStringExtra("msg");
                    tvbill.setText(Html.fromHtml(s1));
                }
                break;
            case 60:
                //优惠券
                if (resultCode == RESULT_OK) {
                    tvYHQ.setText("优惠券: " + data.getStringExtra("msg"));
                    String s1 = "<font ><b>" + "优惠券: " + "</b></font>" + data.getStringExtra("msg");
                    tvYHQ.setText(Html.fromHtml(s1));
                }
                break;

        }


        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getToCheck() {
        String[] split = tvinfomation.getText().toString().split(":");
        String tvInfo = split[1];
        String[] wayArray = tvway.getText().toString().split(":");
        final String way = wayArray[1];
        String[] timeArray = tvtime.getText().toString().split(":");
        String time = timeArray[1];
        String[] billArray = tvbill.getText().toString().split(":");
        String bill = billArray[1];
        String[] quickArray = tvquick.getText().toString().split(":");
        String quick = quickArray[1];
        System.out.println("---" + tvInfo + "-----");
        String addressDetail = name + "\n" + telphone + "\n" + address;
        if (" ".equals(tvInfo)) {
            //ToastUtils.show(getApplicationContext(), "您的信息填写不完整, 请继续填写!!");
            SnackbarUtil.show(getWindow().getCurrentFocus(), "亲!信息不完整哦!!!");
            return;
        }

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("uId", uid);
        params.addQueryStringParameter("addressDetail", addressDetail);
        params.addQueryStringParameter("payway", way);
        params.addQueryStringParameter("sendtime", time);
        params.addQueryStringParameter("invoiceMsg", bill);
        params.addQueryStringParameter("sendType", quick);
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + GlobalConstants.URL_COMIT, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result.toString();
                Gson gson = new Gson();
                SuccessInfo successInfo = gson.fromJson(json, SuccessInfo.class);
                String orderId = successInfo.orderId;
                //payPri1, way
                Intent intent = new Intent(AccountCenterActivity.this, SuccessActivity.class);
                intent.putExtra("id", orderId);
                intent.putExtra("way", way);
                intent.putExtra("price", payPri1.getText());
                startActivity(intent);
                finish();
                EventBus.getDefault().post(new CartInfoUpdateEvent(true));
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.show(getApplicationContext(), "提交清单失败,请查看网络后重新提交!");
                EventBus.getDefault().post(new CartInfoUpdateEvent(false));
            }
        });


    }

    static class SuccessInfo {
        String message;
        String orderId;
        String status;
    }

    static class ViewHolder {
        ImageView picture;
        TextView  num;//数量
        TextView  color;    //颜色
        TextView  size;
        TextView  count;
        TextView  name;
        ImageView delete;
        TextView  price;

    }
}
