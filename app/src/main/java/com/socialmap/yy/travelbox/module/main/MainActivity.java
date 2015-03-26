package com.socialmap.yy.travelbox.module.main;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.socialmap.yy.travelbox.App;
import com.socialmap.yy.travelbox.DebugActivity;
import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.Location;
import com.socialmap.yy.travelbox.module.account.MessageActivity;
import com.socialmap.yy.travelbox.module.account.NearbyActivity;
import com.socialmap.yy.travelbox.module.account.ProfileActivity;
import com.socialmap.yy.travelbox.module.schedule.ScheduleLocalActivity;
import com.socialmap.yy.travelbox.module.team.AllTeamActivity;
import com.socialmap.yy.travelbox.ui.arclibrary.ArcMenu;


public class MainActivity extends FragmentActivity implements SOSFragmentCallBack {
    // 点散菜单
    private static final int[] ITEM_DRAWABLES = {
            R.drawable.composer_thought,
            R.drawable.composer_camera,
            R.drawable.composer_with,
            R.drawable.composer_sleep,
            R.drawable.composer_place
    };
    public int sosnum = 0;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private MyOrientationListener mOrientationListener; // 方向传感器
    private float mCurrentOrientation;  // 方向
    private PoiSearch mPoiSearch = null;
    private EditText find;
    private BDLocationListener mLocationListener = new BDLocationListener() {
        // App 当中的currentLocation并非是实时的位置，这里要获得实时的位置
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 在Main界面注册一个LocationListener，用于处理用户点击定位按钮时产生的定位请求
            // 这里获得的位置相比App 当中的currentLocation 更实时
            Log.d("yy", "main loc");

            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);

            // 在百度地图上设置位置
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mCurrentOrientation)
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);

            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(
                    new LatLng(location.getLatitude(), location.getLongitude())));
        }
    };

    /**
     * 初始化POI搜索
     */
    private void initPOI() {
        // 创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
        // 绑定POI监听器
        mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                //获取POI检索结果
                switch (result.error) {
                    case RESULT_NOT_FOUND:
                        Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
                        break;
                    case NO_ERROR:
                        // 在地图上面显示POI
                        mBaiduMap.clear();
                        PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
                        mBaiduMap.setOnMarkerClickListener(overlay);
                        overlay.setData(result);
                        overlay.addToMap();
                        overlay.zoomToSpan();
                        break;
                    case AMBIGUOUS_KEYWORD:
                        // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                        String strInfo = "在";
                        for (CityInfo cityInfo : result.getSuggestCityList()) {
                            strInfo += cityInfo.city;
                            strInfo += ",";
                        }
                        strInfo += "找到结果";
                        Toast.makeText(MainActivity.this, "在附近找不到", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        // 界面上得POI搜索控件，用户搜索当前位置附近的POI
        find = (EditText) findViewById(R.id.find);
        find.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Location loc = ((App) getApplication()).getCurrentLocation();
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    PoiNearbySearchOption pso = new PoiNearbySearchOption()
                            .location(new LatLng(loc.getLatitude(), loc.getLongitude()))
                                    // TODO POI 使用地图当前位置
                            .radius(1000)
                            .pageCapacity(10)
                            .keyword(find.getText().toString());
                    mPoiSearch.searchNearby(pso);
                }
                return true;
            }
        });
    }

    /**
     * 初始化定位按钮
     */
    private void initLoc() {
        // 定位按钮
        ImageButton loc = (ImageButton) findViewById(R.id.locationself);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取当前位置
                ((App) getApplication()).locationClient.requestLocation();
            }
        });
    }

    /**
     * 初始化SOS
     */
    private void initSOS() {
        // Main界面上的SOS按钮
        ImageButton sos1 = (ImageButton) findViewById(R.id.sos);
        sos1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sosnum == 0) {
                    //SOSDialogFragment sos = new SOSDialogFragment();
                    SOSDialogFragment sos = new SOSDialogFragment("上海");

                    // Supply num input as an argument.
                    sos.show(getFragmentManager(), "SOSDialog");
                } else {
                    SOSCDialogFragment sosc = new SOSCDialogFragment();
                    sosc.show(getFragmentManager(), "SOSCDialog");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main界面进入动画效果，先注释了
        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_main);

        // 隐藏一进入MainActivity的软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // 获取百度地图控件
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL,
                true,
                BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked)
        );
        mBaiduMap.setMyLocationConfigeration(config);

        // 放缩地图到比例尺100米
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(17));

        // 方向变化监听器
        // FIXME 这个方向传感器非常费电，暂且关闭
        /*mOrientationListener = new MyOrientationListener(this);
        mOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                // TODO 方向改变在地图上变化出来
                mCurrentOrientation = x;
                Log.d("yy", "dir change " + x);
            }
        });*/

        // 初始化Main界面当中的POI
        initPOI();

        // 初始化定位按钮
        initLoc();

        // 初始化SOS按钮
        initSOS();

        // 点散菜单
        ArcMenu arcMenu2 = (ArcMenu) findViewById(R.id.arc_menu_2);
        initArcMenu(arcMenu2, ITEM_DRAWABLES);

        // 注册定位监听器
        ((App) getApplication()).locationClient.registerLocationListener(mLocationListener);

    }


    @Override
    protected void onStart() {
        super.onStart();
        // 开启方向传感器
        // FIXME 这个方向传感器非常费电，暂且关闭
        // mOrientationListener.start();

        // 定位到当前位置
        ((App) getApplication()).locationClient.requestLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        // 释放POI检索实例
        mPoiSearch.destroy();

        // 注销在百度LocationClient中的，当MainActivity初始化时注册的监听器
        ((App) getApplication()).locationClient.unRegisterLocationListener(mLocationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();

        // 定位到当前位置
        ((App) getApplication()).locationClient.requestLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        // 停止方向传感器
        // FIXME 这个方向传感器非常费电，暂且关闭
        // mOrientationListener.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_my_schedule: // 日程
                startActivity(new Intent(this, ScheduleLocalActivity.class));
                break;
            case R.id.action_nearby: // 附近
                Intent intent = new Intent(this, NearbyActivity.class);
                intent.putExtra("mainlocal", "上海");
                startActivity(intent);
                break;
            case R.id.action_account: // 我
                Intent intent1 = new Intent(this, ProfileActivity.class);
                intent1.putExtra("mainlocal", "mainlocation");
                startActivity(intent1);
                break;
            case R.id.action_message: // 消息
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.action_allteam: // 团队
                startActivity(new Intent(this, AllTeamActivity.class));
                break;
            case R.id.action_debug: // 调试
                startActivity(new Intent(this, DebugActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化点散菜单
     *
     * @param menu
     * @param itemDrawables
     */
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

    /**
     * 点撒菜单选择
     *
     * @param position
     */
    public void choose(int position) {
        switch (position) {
            case 0: // News
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case 1: // 日程
                startActivity(new Intent(this, ScheduleLocalActivity.class));
                break;
            case 2: // 我（个人资料）
                Intent intent1 = new Intent(this, ProfileActivity.class);
                intent1.putExtra("mainlocal", "上海");
                startActivity(intent1);
                break;
            case 3: // 团队
                startActivity(new Intent(this, AllTeamActivity.class));
                break;
            case 4: // 附近
                Intent intent = new Intent(this, NearbyActivity.class);
                //Log.v("蛤蛤3", mainlocation);
                intent.putExtra("mainlocal", "上海");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void callbackFun1(Bundle arg) {
        sosnum = 0;
    }

    @Override
    public void callbackFun2(Bundle arg) {
        sosnum = sosnum + 1;
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
