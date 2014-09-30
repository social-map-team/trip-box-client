package com.socialmap.yy.travelbox;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.socialmap.yy.travelbox.model.User;

/**
 * Created by yy on 7/21/14.
 */
public class AccountService extends Service {
    public class MyBinder extends Binder {

        //登录验证
        public int login(String username, String password) {
            if (username.equals("yy") && password.equals("yy")) return Global.SUCCESS;
            return Global.ERROR;
        }

        //获取登录之后的User模型
        public User getUser(){
            return null;
        }
    }

    private final String TAG = "AccountService";
    private MyBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind()");
        return binder;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
}
