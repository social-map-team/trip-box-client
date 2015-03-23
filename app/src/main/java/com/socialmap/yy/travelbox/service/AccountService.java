package com.socialmap.yy.travelbox.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.socialmap.yy.travelbox.model.User;
import com.socialmap.yy.travelbox.utils.Global;

import java.io.UnsupportedEncodingException;




public class AccountService extends Service {

    public class MyBinder extends Binder {


        //登录验证
        public int login(String username, String password) {


            if (checkUser(username, password))
                return Global.SUCCESS;
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


    public boolean checkUser(String str1,String str2)

                  {
/*
                  String sql="select * from user where userid=?";

                     Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql, new String[]{str1});

                     if(cursor.moveToFirst())
                     {
                           if(str2.equals(cursor.getString(cursor.getColumnIndex("userpwd"))))
                           {
                                   Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
                                    return true;
                               }
                           else
                           {
                                   Toast.makeText(this, "用户名或者密码错误", Toast.LENGTH_LONG).show();
                                    return false;
                               }
                         }
                      dbHelper.close();
                      return  false;
*/
                     /* getInstance()
                              .request("/api/user/login", "get",
                                      "username", str1,
                                      "password", str2,
                                      "realname", null,
                                      "idcard", null,
                                      "birthday", null,
                                      "phone", null,
                                      "email", null,
                                      "gender", null
                              )
                              .execute(new Callback() {
                                  @Override
                                  public void onFinished(ServerResponse response) {
                                      try {
                                          String content = new String(response.getContent(), "UTF-8");
                                          Log.i("yy", response.getStatusCode() + "\n" + content);

                                      } catch (UnsupportedEncodingException e) {
                                          e.printStackTrace();
                                      }

                                  }
                              });


                      TbsClient.ServerResponse response = new TbsClient.ServerResponse();

                              if(response.getStatusCode() >= 200 && response.getStatusCode() < 300){
                                  return true;
                              }
                                else{
                                  return  false;
                              }*/
                      return true;







              }




}
