package com.socialmap.yy.travelbox.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;

/**
 * Created by gxyzw_000 on 2015/3/20.
 */
public class SOSService extends Service {
    private String sostime;
    private String sosname;
    private String sosphone;
    private String sossay;
    private  String location;
    public  int sostime_int;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //send_sos();

    }






    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        SharedPreferences sp = getSharedPreferences("publiclocal", Context.MODE_WORLD_READABLE);

         location = sp.getString("local","123");
        Log.v("服务",location);

        sostime=intent.getExtras().getString("time");
        sosname=intent.getExtras().getString("name");
        sosphone=intent.getExtras().getString("phone");
        sossay=intent.getExtras().getString("say");

        sostime_int=Integer.parseInt(sostime.trim());

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("爆炸","wobaozal");
    }


    public void send_sos()
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getInstance()
                        .request("/api/sos", "post",
                                "message",sossay,
                                "location",location

                        )
                        .execute(new TbsClient.Callback() {
                            @Override
                            public void onFinished(TbsClient.ServerResponse response) {
                                try {
                                    String content = new String(response.getContent(), "UTF-8");
                                    Log.i("yy", response.getStatusCode() + "\n" + content);

                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

            }
        },sostime_int);

    }









}