package com.tmall.myredboy.utils;
import android.app.Activity;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.picasso.Picasso;
import com.tmall.myredboy.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {

    public static void getHttpDes(final Activity activity, String url, final Class bean, final OnHttpSuccess callBack) {
        OkHttpClient myHttp = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();

        Call call = myHttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.show(activity, "亲，你的网络不给力哦！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Gson gson = new Gson();
                final Object object = gson.fromJson(res, bean);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(object);
                    }
                });
            }
        });
    }

     public static void viewSetImage(Activity activity,String imageUrl,ImageView view){
        Picasso.with(activity).load(imageUrl).placeholder(R.drawable.search_empty).error(R.drawable.address_empty).into(view);

    }

    public static interface OnHttpSuccess {
        void onSuccess(Object object);
    }

    //添加一个post向网络的请求方式 使用okhttp向网络请求的
    /*OkHttpClient client = new OkHttpClient();
    String post(String url, String json) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("platform", "android")
                .add("name", "bug")
                .add("subject", "XXXXXXXXXXXXXXX")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
          在传递参数的时候我传进来一个抽象的类当做参数  在这个方法执行的时候就会回调用抽象类的方法
          在要参数的类就可以获取抽象类中传进来的参数  就将参数带过来了

*/
    public static void   postHttpReques(String Url, final Activity activity, FormBody body, final Class bean , final onPostHttpSuccess Demand) {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url)
                .post(body)
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.show(activity, "获取网络失败");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                System.out.println("httputils"+res);
                Gson gson = new Gson();
              final Object object = gson.fromJson(res, bean);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Demand.PostHttpSuccess(object);
                    }
                });

            }
        });

    }
    public static interface onPostHttpSuccess{
        public void PostHttpSuccess(Object object);
    }

    public void getDataFromServerByPost(String url, HashMap<String,String> paramsMap,
                                        final OnNetResponseListener listener) {
        com.lidroid.xutils.HttpUtils utils = new com.lidroid.xutils.HttpUtils();
        RequestParams params = new RequestParams();
        Iterator iter = paramsMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            params.addBodyParameter(key,val);
        }

        utils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException error, String msg) {
                // 失败
                if(listener != null) {
                    listener.onNotOk(msg);
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 成功
                String result = responseInfo.result;
                if(listener != null) {
                    listener.onOk(result);
                }

            }
        });

    }

    public void getDataFromServerByGet(String url) {

    }

    public interface OnNetResponseListener {
        public void onOk(String json);
        public void onNotOk(String msg);
    }
}
