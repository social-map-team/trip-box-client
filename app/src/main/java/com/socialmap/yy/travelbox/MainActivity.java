package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.socialmap.yy.travelbox.arclibrary.ArcMenu;
import com.socialmap.yy.travelbox.fragment.SOSDialogFragment;
import com.socialmap.yy.travelbox.service.AccountService;


public class MainActivity extends Activity implements AMapLocationListener, LocationSource {

    // Service
    private AccountService.MyBinder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (AccountService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_thought, R.drawable.composer_camera,
             R.drawable.composer_with,R.drawable.composer_sleep,R.drawable.composer_place };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);
        //初始化高德地图
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();

        ImageButton sos = (ImageButton) findViewById(R.id.sos);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);



        ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);

        initArcMenu(arcMenu2, ITEM_DRAWABLES);
















        sos.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                        SOSDialogFragment sos = new SOSDialogFragment();
                        sos.show(getFragmentManager(), "SOSDialog");
                    }
                });




                //绑定账户服务
        bindService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"),
                conn,
                Service.BIND_AUTO_CREATE);


















        //测试添加地图标记
        //TODO 实现日程中地点点击后跳转到地图上面显示具体地点
       /* MarkerOptions mo = new MarkerOptions();
        mo.title("This is title.");
        mo.position(new LatLng(39.9073, 116.3911));
        mo.snippet("This is snippet");
        mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.anchor_mao_small));


        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View root = getLayoutInflater().inflate(R.layout.activity_main_info_windows, null);
                TextView content = (TextView) root.findViewById(R.id.content);
                content.setText("！");
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/mao.ttf");
                content.setTypeface(typeFace);
                return root;
            }
        });

        aMap.addMarker(mo).showInfoWindow();

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MainActivity.this, "！", Toast.LENGTH_SHORT).show();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MainActivity.this, "！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

*/
    }



    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.anchor_mao));
        myLocationStyle.strokeColor(Color.BLUE);
        myLocationStyle.strokeWidth(5);
        aMap.setMyLocationStyle(myLocationStyle);
        mAMapLocationManager = LocationManagerProxy.getInstance(MainActivity.this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
   /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60*1000, 10, this);

        }
    }



    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    //主界面中菜单点击事件响应
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_my_schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.action_nearby:
                startActivity(new Intent(this, NearbyActivity.class));
                break;
            case R.id.action_account:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
          /*  case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_sos:
                //show sos dialog
                SOSDialogFragment sos = new SOSDialogFragment();
                sos.show(getFragmentManager(), "SOSDialog");
                break;
            case R.id.action_feedback:
                startActivity(new Intent(this, ComplainActivity.class));
                break;  */
            case R.id.action_message:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.action_allteam:
                startActivity(new Intent(this, AllTeamActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


//TODO SOS

    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    choose(position);
                   // Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();








                }
            });
        }
    }


   public void choose(int position) {
       switch (position) {
           case 0:
               startActivity(new Intent(this, MessageActivity.class));
               break;
           case 1:
               startActivity(new Intent(this, ScheduleActivity.class));
               break;
           case 2:
               startActivity(new Intent(this, ProfileActivity.class));
               break;
           case 3:
               startActivity(new Intent(this, AllTeamActivity.class));
               break;
           case 4:
               startActivity(new Intent(this, NearbyActivity.class));
               break;


       }


   }




}
