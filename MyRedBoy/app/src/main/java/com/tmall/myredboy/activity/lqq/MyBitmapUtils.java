package com.tmall.myredboy.activity.lqq;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tmall.myredboy.global.GlobalConstants;

import java.io.File;

import static android.R.attr.bitmap;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MyBitmapUtils {

    public void getSelectrorBitMap(String twoUrl, final ImageView ivImage) {

        final HttpUtils httpUtils = new HttpUtils();
        final String[] url = twoUrl.split(",");

        final String fileName1 = "/sdcard/" + url[0].substring(url[0].lastIndexOf("/") + 1);
        final String fileName2 = "/sdcard/" + url[1].substring(url[1].lastIndexOf("/") + 1);

        //加载第一张图片
        httpUtils.download(GlobalConstants.URL_NET_IMAGE + url[0], fileName1, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                final Bitmap bitmapRed = BitmapFactory.decodeFile(fileName1);

                //加载第二张图片
                httpUtils.download(GlobalConstants.URL_NET_IMAGE + url[1], fileName2, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        Bitmap bitmapWrite = BitmapFactory.decodeFile(fileName2);
                        //适配器
                        setSelector(bitmapRed, bitmapWrite, ivImage);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                    }
                });
            }
            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void setSelector(Bitmap bitmap1, Bitmap bitmap2, ImageView ivImage) {
        // new出一个状态选择器的对象
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(bitmap2));
        stateListDrawable.addState(new int[]{}, new BitmapDrawable(bitmap1));

        ivImage.setImageDrawable(stateListDrawable);
    }

    public void getBitmap(final ImageView ivImage, String url) {
        HttpUtils httpUtils = new HttpUtils();
        final String file = "/sdcard/" + url;
        httpUtils.download(GlobalConstants.URL_NET_IMAGE + url, file, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Bitmap bitmapWrite = BitmapFactory.decodeFile(file);
                ivImage.setImageBitmap(bitmapWrite);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

}
