package com.tmall.myredboy.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.tmall.myredboy.activity.SplashActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import okhttp3.Call;
import okhttp3.Response;

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();

        File file = null;
        try {
            //收集错误日志, 将日志文件自动上传到服务器, 由开发者分析
            long l = System.currentTimeMillis();
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/err/", "err" + l + ".log");
            PrintWriter err = new PrintWriter(file);
            e.printStackTrace(err);
            err.flush();
            err.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (file != null) {
            OkHttpUtils.postFile()
                    .url("http://192.168.191.1:8080/0")
                    .file(file)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int i) throws Exception {
                            return null;
                        }

                        @Override
                        public void onError(Call call, Exception e, int i) {
                        }

                        @Override
                        public void onResponse(Object o, int i) {
                        }
                    });
        }
        Intent intent = new Intent(getApplicationContext(),
                SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, 0);

        //定时器
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1500,
                restartIntent); // 1.5秒钟后重启应用

        //退出应用程序
        System.exit(-1);
    }
}
