package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yy on 7/22/14.
 */
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
    MapView mapView;
    AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_main);

        //绑定账户服务
        bindService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"),
                conn,
                Service.BIND_AUTO_CREATE);

        //初始化高德地图
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();

        //测试添加地图标记
        //TODO 实现日程中地点点击后跳转到地图上面显示具体地点
        MarkerOptions mo = new MarkerOptions();
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
                View root = getLayoutInflater().inflate(R.layout.activity_main_info_windows,null);
                TextView content = (TextView)root.findViewById(R.id.content);
                content.setText("同志们好，同志们辛苦了！");
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/mao.ttf");
                content.setTypeface(typeFace);
                return root;
            }
        });

        aMap.addMarker(mo).showInfoWindow();

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MainActivity.this,"毛主席万岁,万岁，万万岁！",Toast.LENGTH_SHORT).show();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MainActivity.this,"毛主席万岁！",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


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
        myLocationStyle.strokeWidth(10);
        aMap.setMyLocationStyle(myLocationStyle);
        mAMapLocationManager = LocationManagerProxy.getInstance(this);
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
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 5000, 10,this);
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
            case R.id.action_shop:
                startActivity(new Intent(this, ShopActivity.class));
                break;
            case R.id.action_account:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_sos:
                //show sos dialog
                SOSDialogFragment sos = new SOSDialogFragment();
                sos.show(getFragmentManager(), "SOSDialog");
                break;
            case R.id.action_feedback:
                startActivity(new Intent(this, ComplainActivity.class));
                break;
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

    public static class SOSDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            View v = inflater.inflate(R.layout.dialog_sos, null);
            EditText message = (EditText) v.findViewById(R.id.message);
            message.addTextChangedListener(new TextWatcher() {


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    count = 0;
                }
            });

            //TODO first use EditText, now I use OnClickListener to detect it.
            message.setOnClickListener(new View.OnClickListener() {
                boolean firstChange = true;

                @Override
                public void onClick(View v) {
                    if (firstChange) {
                        maxCount = 300;
                        count = 0;
                        firstChange = false;
                    }
                }
            });

            builder.setView(v)
                    // Add action buttons
                    .setPositiveButton(R.string.sos_send, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            timer.cancel();
                            //TODO send sos message
                        }
                    })
                    .setNegativeButton(R.string.sos_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            timer.cancel();
                            SOSDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what >= 0) {
                    Button send = ((AlertDialog) SOSDialogFragment.this.getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
                    send.setText(getString(R.string.sos_send) + "(" + msg.what + ")");
                }
            }
        };
        private Timer timer = new Timer();
        private int maxCount = 5;
        private int count = 0;
        private static int UPDATE_BUTTON_TEXT = 1;
        private TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                if (count <= maxCount) {
                    handler.sendEmptyMessage(maxCount - count);
                    count++;
                } else {
                    timer.cancel();
                    //TODO auto send sos
                    //close dialog
                    SOSDialogFragment.this.getDialog().cancel();
                }
            }
        };

        @Override
        public void onStart() {
            super.onStart();
            timer.schedule(timerTask, 0, 1000);
        }
    }
}
