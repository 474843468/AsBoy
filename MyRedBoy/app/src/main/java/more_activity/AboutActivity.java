package more_activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.bean.VersionInfo;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.ToastUtils;

import java.io.File;

public class AboutActivity extends AppCompatActivity {

    private TextView version;
    private int versionCode;
    private VersionInfo versionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.title_tv);
        tvTitle.setText("关于");
        version = (TextView) findViewById(R.id.tv_version);
        version.setText(getVersionName());
        checkVersion();

    }

    public String getVersionName() {

        PackageManager pm = getPackageManager();


        PackageInfo packageInfo = null;
        try {
            packageInfo = pm
                    .getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;//版本名称
            String versionCode = String.valueOf( packageInfo.versionCode);

            System.out.println("versionName:" + versionName);
            System.out.println("versionCode:" + this.versionCode);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getVersionCode() {

        PackageManager pm = getPackageManager();


        PackageInfo packageInfo = null;
        try {
            packageInfo = pm
                    .getPackageInfo(getPackageName(), 0);

            String versionName = packageInfo.versionName;//版本名称
            String versionCode = String.valueOf( packageInfo.versionCode);


            System.out.println("versionName:" + versionName);
            System.out.println("versionCode:" + versionCode);

            return  versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkVersion() {
        HttpUtils utils = new HttpUtils();

        //设置连接超时时间
        utils.configTimeout(5000);
       utils.send(HttpRequest.HttpMethod.GET, GlobalConstants.URL_PREFIX + "index/index_checkVersion.html?&ver=1.0", new RequestCallBack<String>() {
           @Override
           public void onSuccess(ResponseInfo<String> responseInfo) {
               parserJson(responseInfo.result);
           }

           @Override
           public void onFailure(HttpException e, String s) {
                    e.printStackTrace();
           }
       });
    }
      public void parserJson(String result){
        Gson gson = new Gson();
          versionInfo = gson.fromJson(result, VersionInfo.class);
          if(versionInfo.status==200){
              String versionCode = versionInfo.version.versionCode;
              if(versionCode.compareTo(getVersionCode())>0) {
                  showUpdateDialog();
              }  else{
                  ToastUtils.show(getApplicationContext(),"已经是最新版本!");
              }
          } else{
              ToastUtils.show(getApplicationContext(),"网络访问失败!");
          }

    }
    public void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("发现新版本"+versionCode);
        builder.setMessage(versionInfo.version.versionMessage);
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk();
                    }


                });
        builder.setNegativeButton("以后再说",null);
        AlertDialog dialog = builder.create();
        //显示dialog
        dialog.show();

    }

    private void downloadApk() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向进度条, 带进度
        dialog.setMessage("正在下载,请稍候...");
        dialog.setCancelable(false);//禁止取消弹窗, 点弹窗外侧,或返回键,弹窗不消失
        dialog.show();

        String mUrl = versionInfo.version.downloadUrl;
         //  String targer= getFilesDir().getAbsolutePath()+"/RedBoy.apk";
        String targer = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/RedBoy.apk";

        HttpUtils utils = new HttpUtils();
        //参2: 下载的文件的目标地址
        utils.download(mUrl, targer, new RequestCallBack<File>() {

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //开始安装
                System.out.println("下载成功!");
                File file = responseInfo.result;//下载后的文件对象

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                startActivityForResult(intent, 0);

                //隐藏弹窗
                dialog.dismiss();
            }

            //下载进度
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                //total:文件总长度
                //current: 已加载长度
                int percent = (int) (current * 100 / total);
                dialog.setProgress(percent);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(AboutActivity.this, "下载失败!", Toast.LENGTH_SHORT)
                        .show();

                //隐藏弹窗
                dialog.dismiss();
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("用户取消安装了");
        //如果程序能走到这一步, 说明程序没有被覆盖, 安装失败, 跳主页面
      ToastUtils.show(getApplicationContext(),"安装成功!");
    }
}
