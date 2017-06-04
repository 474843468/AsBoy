package com.tmall.myredboy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;

/**
 * Created by Administrator on 2016/11/16.
 */

public class MyGallery extends Gallery {

//    private Context context;

    public MyGallery(Context context) {
        this(context, null);
    }

    public MyGallery(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        // TODO Auto-generated constructor stub
    }

    public MyGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        this.context = context;
    }

    public void setImageActivity(GoodsDetailActivity context) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {

        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return false;

    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
}
