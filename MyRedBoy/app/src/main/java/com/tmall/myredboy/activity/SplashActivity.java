package com.tmall.myredboy.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.tmall.myredboy.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ivImage = (ImageView) findViewById(R.id.ivImage);

        // 1、缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        // 2、渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.6f, 1.0f);
        alphaAnimation.setDuration(4000);
        alphaAnimation.setFillAfter(true);

        //TranslateAnimation
        // 将2个动画添加到动画集合中
        AnimationSet animationSet = new AnimationSet(true);//是否共享动画的插入器
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        //启动动画
        ivImage.startAnimation(animationSet);

        //设置所有图片的Xml文件
        ivImage.setBackgroundResource(R.drawable.amin);
        //获取帧动画
        final AnimationDrawable splashAnimation = (AnimationDrawable) ivImage.getBackground();
        //开始播放
        splashAnimation.start();

        //监听动画结束
        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                splashAnimation.stop();
                finish();
            }
        });

        findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                ivImage.clearAnimation();
                splashAnimation.stop();
                finish();
            }
        });
    }
}
