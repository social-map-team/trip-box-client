package com.socialmap.yy.travelbox;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by yy on 3/24/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
