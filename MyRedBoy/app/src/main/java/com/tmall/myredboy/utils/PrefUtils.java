package com.tmall.myredboy.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 */
public class PrefUtils {

    //存储String类型数据
    public static void putString(Context context, String key, String value) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //存储数据
        sp.edit().putString(key, value).commit();
    }

    //获取String类型数据
    public static String getString(Context context, String key, String defValue) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取数据
        return sp.getString(key, defValue);
    }

    //存储boolean类型数据
    public static void putBoolean(Context context, String key, boolean value) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //存储数据
        sp.edit().putBoolean(key, value).commit();
    }

    //获取boolean类型数据
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取数据
        return sp.getBoolean(key, defValue);
    }

    //存储int类型数据
    public static void putInt(Context context, String key, int value) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //存储数据
        sp.edit().putInt(key, value).commit();
    }

    //获取int类型数据
    public static int getInt(Context context, String key, int defValue) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //获取数据
        return sp.getInt(key, defValue);
    }

    //移除数据
    public static void remove(Context context, String key) {
        //获取SharedPreferences对象
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        //移除数据
        sp.edit().remove(key).commit();
    }

}
