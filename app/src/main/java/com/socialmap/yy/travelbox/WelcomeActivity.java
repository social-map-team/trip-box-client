package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.socialmap.yy.travelbox.utils.DimensionUtils;


public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //启动账户服务
        startService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"));

        //Initialize the whole application
        DimensionUtils.init(getApplicationContext());

        TbsClient.init(this);

        TextView textView = (TextView) findViewById(R.id.welcome_title);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Sketch Gothic School.ttf");
        textView.setTypeface(typeFace);

        //Load background image
        // 获取Android屏幕的服务
        //WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        //Point size = new Point();
        //wm.getDefaultDisplay().getSize(size);
        //int windowHeight = size.y;
        //int windowWidth = size.x;
        //Log.i("yy", windowHeight + "," + windowWidth);
        //ImageView image = (ImageView)findViewById(R.id.background);
        //TODO 这里设置成1920*1080会出现图片太大不能加载的问题
        //image.setImageBitmap(ImageHelper.decodeSampledBitmapFromResource(getResources(), R.drawable.welcome, 160, 90));

        //延迟3000ms切换到主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);

    }
}
