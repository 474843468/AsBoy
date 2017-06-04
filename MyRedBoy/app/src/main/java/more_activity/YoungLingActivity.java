package more_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tmall.myredboy.R;
import com.tmall.myredboy.global.GlobalConstants;

public class YoungLingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_young_ling);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       WebView webview = (WebView) findViewById(R.id.wv);
        Intent intent = getIntent();

        String url = intent.getStringExtra("url");
        //获取WebView的设置对象
        WebSettings settings = webview.getSettings();

        //启用js
        settings.setJavaScriptEnabled(true);
        //显示放大缩小按钮  同时还能支持双指缩放
        settings.setBuiltInZoomControls(true);
        //支持双击缩放
        settings.setUseWideViewPort(true);

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.loadUrl(GlobalConstants.URL_PREFIX+url);

    }
}
