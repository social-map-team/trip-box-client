package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by yy on 7/24/14.
 */
public class ShopActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        WebView web = (WebView)findViewById(R.id.shop_web);
        web.loadUrl("http://www.starbucks.com/");
    }
}
