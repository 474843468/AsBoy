package com.tmall.myredboy.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 全局吐司工具类
 */
public class ToastUtils {

    //创建一个有主线程轮询器的Handler
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 全局吐司
     * @param context
     *         上下文对象
     * @param text
     *         吐司内容
     */
    public static void show(final Context context, final String text) {
        //判断当前线程轮询器是否时主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            //如果在主线程, 直接吐司
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            //如果不在主线程, 用Handler传递到主线程吐司
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
