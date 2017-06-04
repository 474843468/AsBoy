package com.tmall.myredboy.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.AddressInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.ToastUtils;
import com.tmall.myredboy.wheelview.ChangeAddressPopwindow;

public class AddressUpdateActivity extends BaseActivity {

    private EditText                etName;
    private EditText                etPhone;
    private EditText                etArea;
    private TextView                tvAddress;
    private boolean                 isUpdate;
    private ImageView               ivDelete;
    private AddressInfo.AddressBean mDatas;
    private String                  uId;
    private boolean isDelete = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_insert);
        uId = PrefUtils.getString(this, GlobalConstants.PREF_USER_ID, "");

        tvCenter.setText("新增地址");
        btnRight.setText("保存");
        btnRight.setVisibility(View.VISIBLE);
        //保存按钮点击事件
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitNewAddress();
            }
        });

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etArea = (EditText) findViewById(R.id.etArea);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                ChangeAddressPopwindow mChangeAddressPopwindow = new ChangeAddressPopwindow(AddressUpdateActivity.this);
                mChangeAddressPopwindow.setAddress("陕西", "西安", "雁塔区");
                mChangeAddressPopwindow.showAtLocation(tvAddress, Gravity.BOTTOM, 0, 0);
                mChangeAddressPopwindow
                        .setAddresskListener(new ChangeAddressPopwindow.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                tvAddress.setText((province + city + area).trim());
                            }
                        });
            }
        });

        ivDelete = (ImageView) findViewById(R.id.ivDelete);
        LinearLayout llBtn = (LinearLayout) findViewById(R.id.llBtn);
        //设为默认地址
        findViewById(R.id.btnDefault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultAddress();
            }
        });
        //删除地址
        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDelete = true;
                deleteAddress();
            }
        });

        //获取联系人
        findViewById(R.id.ivContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), 0);
            }
        });

        Intent intent = getIntent();
        isUpdate = intent.getBooleanExtra("isUpdate", false);
        if (isUpdate) {
            tvCenter.setText("修改地址");
            llBtn.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etName.setText("");
                }
            });
            mDatas = (AddressInfo.AddressBean) intent.getSerializableExtra("data");
            etName.setText(mDatas.name);
            etPhone.setText(mDatas.telphone);
            tvAddress.setText(mDatas.address);
            etArea.setText(mDatas.area);
        }

    }

    //删除地址
    private void deleteAddress() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("addressId", String.valueOf(mDatas.id));
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "address/address_delete.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //String json = responseInfo.result;
                //  JSONObject jo = null;
                //try {
                //  jo = new JSONObject(json);
                //  String status = jo.getString("status");
                //  ToastUtils.show(AddressUpdateActivity.this, "删除成功"+status);
                //} catch (JSONException e) {
                //  e.printStackTrace();
                //}
                //扫描数据都可以保存,直接吐司
                if (isDelete) {
                    ToastUtils.show(AddressUpdateActivity.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.show(AddressUpdateActivity.this, "网络正忙,请稍候重试...");
            }
        });
    }

    //设为默认地址
    private void setDefaultAddress() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("addressId", String.valueOf(mDatas.id));
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "address/address_setDefault.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                ToastUtils.show(AddressUpdateActivity.this, "默认地址设置成功！");
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.show(AddressUpdateActivity.this, "网络正忙,请稍候重试...");
            }
        });
    }

    //提交新增地址
    private void commitNewAddress() {
        if (isUpdate) {
            isDelete = false;
            deleteAddress();
        }
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = tvAddress.getText().toString().trim();
        String area = etArea.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(area)) {
            ToastUtils.show(this, "数据不能为空，请完善数据!");
            return;
        }
        if (!phone.matches("1[3-8]\\d{9}")) {
            ToastUtils.show(this, "手机号码格式错误,请重试！");
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configTimeout(5000);
        RequestParams requestParams = new RequestParams();
//        requestParams.addHeader();
        requestParams.addQueryStringParameter("uId", uId);
        requestParams.addQueryStringParameter("address_name", name);
        requestParams.addQueryStringParameter("address_telephone", phone);
        requestParams.addQueryStringParameter("address_province", address);
        requestParams.addQueryStringParameter("address_area", area);
        requestParams.addQueryStringParameter("address_isDefault", "0");
        httpUtils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + "address/address_save.html", requestParams, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //扫描数据都可以保存,直接吐司
                ToastUtils.show(AddressUpdateActivity.this, "保存成功");
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.show(AddressUpdateActivity.this, "网络正忙,请稍候重试...");
            }
        });
    }

    //获取系统联系人
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            //获取联系人uri
            Uri contactData = data.getData();
            Cursor cursor = resolver.query(contactData, null, null, null, null);
            cursor.moveToFirst();
            //查询联系人id
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            //根据联系人id查询电话号码
            Cursor phone = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            String number = "";
            if (phone.moveToNext()) {
                //获取电话号码
                number = phone
                        .getString(phone
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            number = number.replace("-", "").replace(" ", "");//去掉"-"和空格
            //更新EditText
            etPhone.setText(number);
        }

    }

}
