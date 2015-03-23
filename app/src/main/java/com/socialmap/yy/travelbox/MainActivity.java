package com.socialmap.yy.travelbox;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.socialmap.yy.travelbox.arclibrary.ArcMenu;
import com.socialmap.yy.travelbox.call.SOSFragmentCallBack;
import com.socialmap.yy.travelbox.fragment.SOSCDialogFragment;
import com.socialmap.yy.travelbox.fragment.SOSDialogFragment;
import com.socialmap.yy.travelbox.listener.MyOrientationListener;
import com.socialmap.yy.travelbox.service.AccountService;


public class MainActivity extends FragmentActivity implements SOSFragmentCallBack,OnGetPoiSearchResultListener {
    MapView mMapView = null;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    private BaiduMap mBaiduMap;
    public TextView mLocationResult,logMsg;
    boolean isFirstLoc = true;// 是否是第一次定位
    private boolean isRequest = false;//手动触发定位请求
    BitmapDescriptor mCurrentMarker;
    private ImageButton localbutton;
    private String  mainlocation="上海";
    // 自定义定位图标
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private MyLocationConfiguration.LocationMode mLocationMode;
    private PoiSearch mPoiSearch = null;
    private EditText find;

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

    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_thought, R.drawable.composer_camera,
             R.drawable.composer_with,R.drawable.composer_sleep,R.drawable.composer_place };
    public int sosnum=0   ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);
       //poi检索
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);


        //初始化百度地图
        mLocationClient = new LocationClient(this.getApplicationContext());

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mCurrentMarker = null;
        mBaiduMap.setMyLocationEnabled(true);

         mLocationResult = (TextView)findViewById(R.id.local);
        mLocationResult = new TextView(this.getApplicationContext());

       // mLocationResult = new LocationResult(this.getApplicationContext());





        //放缩地图
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(19);
        mBaiduMap.animateMapStatus(u);


        //定位SDK
        mMyLocationListener = new MyLocationListener();
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener( mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //option.setOpenGps(true);// 开启GPS
        option.setCoorType("bd09ll"); // 编码有三种,gcj02  bd09   bd0911
        option.setScanSpan(2000);//这个是设置定位间隔时间，单位ms
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        //定义指向图标
        mIconLocation = BitmapDescriptorFactory
                .fromResource(R.drawable.navi_map_gps_locked);
        myOrientationListener = new MyOrientationListener(this);

        mLocationClient.start();
        requestLocation();


        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener()
                {
                    @Override
                    public void onOrientationChanged(float x)
                    {
                        mCurrentX = x;
                    }
                });


        //手动定位
        /**  selfloc = (ImageButton)findViewById(R.id.locationself);
         View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
        if (view.equals(selfloc)) {
        requestLocation();}
        }
        };
         selfloc.setOnClickListener(onClickListener);**/  //TODO 方法一

//TODO 方法二
       ImageButton localbutton = (ImageButton) findViewById(R.id.locationself);

               localbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                requestLocation();
            }
        });





        ImageButton sos1 = (ImageButton) findViewById(R.id.sos);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);



        ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);

        initArcMenu(arcMenu2, ITEM_DRAWABLES);




        find = (EditText)findViewById(R.id.find);

        //PoiNearbySearchOption pso= new PoiNearbySearchOption().location( mMyLocationListener.ll1).radius(100).pageCapacity(20).keyword(find.getText().toString());

        find.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    PoiNearbySearchOption pso= new PoiNearbySearchOption().location( mMyLocationListener.ll1).radius(1000).pageCapacity(10).keyword(find.getText().toString());
                    mPoiSearch.searchNearby(pso);
                    Log.v("附近",mMyLocationListener.ll1.toString());
                    Log.v("附近1",mPoiSearch.toString());


                }
                return false;

            }
        });







        //Log.v("习习蛤蛤3",mLocationResult.getText().toString());

        //mainlocation=mLocationResult.getText().toString();
        //Log.v("蛤蛤2",mainlocation);


        sos1.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                        if(sosnum==0){
                        //SOSDialogFragment sos = new SOSDialogFragment();
                          //

                                SOSDialogFragment sos = new SOSDialogFragment(mainlocation);

                                // Supply num input as an argument.
                                sos.show(getFragmentManager(), "SOSDialog");
                            }
                        else {
                            SOSCDialogFragment sosc = new SOSCDialogFragment();
                            sosc.show(getFragmentManager(), "SOSCDialog");

                        }
                    }
                });




                //绑定账户服务
        bindService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"),
                conn,
                Service.BIND_AUTO_CREATE);



    }




    @Override
    protected void onStart()
    {
        super.onStart();
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        // 开启方向传感器
        myOrientationListener.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mPoiSearch.destroy();

    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 停止方向传感器
        myOrientationListener.stop();

    }





    // 定位监听
    public class MyLocationListener implements BDLocationListener {
        private LatLng ll1;
        @Override
        public void onReceiveLocation(BDLocation location) {


            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(mCurrentX)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc|| isRequest) {
                isFirstLoc = false;
                isRequest = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                ll1=ll;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }

            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);



            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());

                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            logMsg(sb.toString());  //TODO 这就是把经纬度传出来的代码
            //Log.v("习习蛤蛤1",mLocationResult.getText().toString());
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
        //TODO 这个就是接收经纬度的。对应的是189行
     public void logMsg(String str) {
         try {
         //if (mLocationResult != null)
             mLocationResult.setText(str);  //TODO locationresult就是定位结果，log里面也能查到。这里用的是TEXTVIEW显示，而我们需要的是服务器
             mainlocation=mLocationResult.getText().toString();
             //Log.v("习习蛤蛤2",mLocationResult.getText().toString());
         } catch (Exception e) {
         e.printStackTrace();
         }
         }


        public void onReceivePoi(BDLocation poiLocation) {
        }


    /**
     * 手动请求定位的方法
     */
    public void requestLocation() {
        isRequest = true;

        if(mLocationClient != null && mLocationClient.isStarted()){
            mLocationClient.requestLocation();

        }else{
            Log.d("log", "locClient is null or not started");
        }
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
                startActivity(new Intent(this, ScheduleLocalActivity.class));
                break;
            case R.id.action_nearby:
                Intent intent = new Intent(this, NearbyActivity.class);
                intent.putExtra("mainlocal", mainlocation);
                startActivity(intent);
                break;
            case R.id.action_account:
                Intent intent1 = new Intent(this, ProfileActivity.class);
                intent1.putExtra("mainlocal", mainlocation);
                startActivity(intent1);
                break;
            case R.id.action_message:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.action_allteam:
                startActivity(new Intent(this, AllTeamActivity.class));
                break;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
               Intent intent1 = new Intent(this, ProfileActivity.class);
               intent1.putExtra("mainlocal", mainlocation);
               startActivity(intent1);
               break;
           case 3:
               startActivity(new Intent(this, AllTeamActivity.class));
               break;
           case 4:
               Intent intent = new Intent(this, NearbyActivity.class);
               Log.v("蛤蛤3",mainlocation);
               intent.putExtra("mainlocal", mainlocation);
               startActivity(intent);
               break;
       }
   }

    @Override
    public void callbackFun1(Bundle arg) {

            sosnum=0;
    }

    @Override
    public void callbackFun2(Bundle arg) {

        sosnum=sosnum+1;
    }








    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MainActivity.this, "在附近找不到", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }
















}
