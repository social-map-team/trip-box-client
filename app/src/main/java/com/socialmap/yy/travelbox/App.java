package com.socialmap.yy.travelbox;

import android.app.Application;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
                if (location == null) return;
                if (location.getAddrStr() != null) {
                    // 只有使用网络定位的情况下，才能获取当前位置的反地理编码描述。
                    Log.d("yy", "App中记录当前位置：" + location.getAddrStr());
                }
                Log.d("yy", "App中记录当前位置：[经度]" + location.getLongitude());
                Log.d("yy", "App中记录当前位置：[纬度]" + location.getLatitude());
                Log.d("yy", "App中记录当前位置：[方向]" + location.getDirection());

                currentLocation = new Location(location);
            }
        });

        LocationClientOption option = new LocationClientOption();

        // Hight_Accuracy高精度、Battery_Saving低功耗、Device_Sensors仅设备(GPS)
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        // 设置打开GPS
        option.setOpenGps(true);

        // 设置返回值的坐标类型
        // 国测局经纬度坐标系 gcj02
        // 百度墨卡托坐标系 bd09
        // 百度经纬度坐标系 bd09ll
        // 百度手机地图对外接口中的坐标系默认是bd09ll
        option.setCoorType("bd09ll");

        //这个是设置定位间隔时间，单位ms
        //option.setScanSpan(2000);

        //设置是否要返回地址信息，默认为无地址信息。
        option.setAddrType("all");
        
        // 在网络定位中，获取手机机头所指的方向
        option.setNeedDeviceDirect(true);
        locationClient.setLocOption(option);

        // FIXME 为了调试方便，暂且程序一启动就开启定位服务
        locationClient.start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (locationClient.isStarted()) locationClient.stop();
    }
}
