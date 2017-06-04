package com.tmall.myredboy.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tmall.myredboy.db.ScanlogOpenhelper;
import java.util.HashSet;

/**
 * Created by Administrator on 2016/11/22.
 */

public class ScanLogDao {

    private ScanlogOpenhelper scanlogOpenhelper;
    //单例模式(懒汉式),定义一个helper对象
    private static ScanLogDao scanLogDao = null;
    private Context context;

    //私有构造方法
    private ScanLogDao(Context context) {
        scanlogOpenhelper = new ScanlogOpenhelper(context);
        this.context = context;
    }

    //提供获取实例的方法
    public static ScanLogDao getInstance(Context context) {
        if (scanLogDao == null) {
            //加锁
            synchronized (ScanLogDao.class) {
                if (scanLogDao == null) {
                    scanLogDao = new ScanLogDao(context);
                }
            }

        }
        return scanLogDao;
    }

    //添加浏览的记录
    public boolean addScanLog(Context context, String proId) {
        SQLiteDatabase sqLiteDatabase = scanlogOpenhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("proId", proId);
        long insert = sqLiteDatabase.insert("scan", null, values);
        sqLiteDatabase.close();
        return insert != -1;
    }

    //取出浏览的记录
    public HashSet<String> getScanLog() {
        SQLiteDatabase sqLiteDatabase = scanlogOpenhelper.getReadableDatabase();
        HashSet<String> set = new HashSet<String>();
        Cursor cursor = sqLiteDatabase.query("scan", new String[]{"proId"}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String proId = cursor.getString(0);
                set.add(proId);
            }
            cursor.close();
        }
        sqLiteDatabase.close();
        return set;
    }
}