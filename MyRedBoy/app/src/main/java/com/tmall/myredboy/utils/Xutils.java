package com.tmall.myredboy.utils;

import android.content.Context;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class Xutils {

    public static void setIv(Context ct, ImageView imageView, String url){
	   BitmapUtils utils = new BitmapUtils(ct);
	   utils.display(imageView, url);
    }

    /*public static String getString(String url){*/
/*	   HttpUtils utils = new HttpUtils();*/
/*	   utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {*/
/*		  @Override*/
/*		  public void onSuccess(ResponseInfo<String> responseInfo) {*/
/*			 */
/*		  }*/
/*
*/

/*		  @Override*/
/*		  public void onFailure(HttpException e, String s) {*/
/*			 return "";*/
/*		  }*/
/*	   });*/
    /*}*/

}
