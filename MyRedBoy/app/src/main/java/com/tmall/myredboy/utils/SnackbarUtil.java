package com.tmall.myredboy.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tmall.myredboy.R;

/**
 * 底部提示框
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        setTextColor(snackbar, Color.parseColor("#FFFFFF"));
        setTextSize(snackbar, TypedValue.COMPLEX_UNIT_SP, 16);
        setTextGravity(snackbar, Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    //设置字体颜色
    public static void setTextColor(Snackbar snackbar, int color) {
        //获取snackbar的显示控件
        View view = snackbar.getView();
        //修改属性
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    //设置字体大小
    public static void setTextSize(Snackbar snackbar, int unit, float size) {
        //获取snackbar的显示控件
        View view = snackbar.getView();
        //修改属性
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextSize(unit, size);
    }

    //设置对其方式
    public static void setTextGravity(Snackbar snackbar, int gravity) {
        //获取snackbar的显示控件
        View view = snackbar.getView();
        //修改属性
        ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(gravity);
    }
}
