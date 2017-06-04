package com.tmall.myredboy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ScanlogOpenhelper extends SQLiteOpenHelper {
    //构造方法
    public ScanlogOpenhelper(Context context) {
        super(context, "scanlog.db", null, 1);
    }

    //创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        //写一个创建数据库的SQL语句
        String sql = "create table scan(_id integer primary key autoincrement, proId String)";
        db.execSQL(sql);
    }

    //版本升级的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
