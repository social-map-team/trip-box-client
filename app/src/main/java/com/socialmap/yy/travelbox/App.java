package com.socialmap.yy.travelbox;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.socialmap.yy.travelbox.model.Location;
import com.socialmap.yy.travelbox.model.User;

/**
 * Created by yy on 3/24/15.
 */
public class App extends Application {

    public LocationClient locationClient = null;

    private Location currentLocation; // 注意这不是当前是实时位置，这里记录用户上一次Main界面点击定位按钮所得到的位置

    private boolean login;

    private User currentUser;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public boolean isLogin() {
        return login;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        if (currentUser == null)
            login = false;
        else
            login = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        // 初始化百度地图：定位SDK
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.d("yy", "app loc");
                if (location == null) return;
                currentLocation = new Location(location);
            }
        });

        // FIXME 暂且关闭，2秒间隔定位一次
        /*LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //option.setOpenGps(true);    // 开启GPS
        option.setCoorType("bd09ll"); // 编码有三种,gcj02  bd09   bd0911
        option.setScanSpan(2000);     //这个是设置定位间隔时间，单位ms
        option.setAddrType("all");
        locationClient.setLocOption(option);*/

        // FIXME 为了调试方便，暂且程序一启动就开启定位服务
        locationClient.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (locationClient.isStarted()) locationClient.stop();
    }
}
