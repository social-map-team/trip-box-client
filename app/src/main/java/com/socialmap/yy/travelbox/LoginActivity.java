package com.socialmap.yy.travelbox;

import  android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.utils.Global;

import com.socialmap.yy.travelbox.service.AccountService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

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
/**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //绑定账户服务
        bindService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"),
                conn,
                Service.BIND_AUTO_CREATE);

        TextView textView = (TextView) findViewById(R.id.welcome_title);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Sketch Gothic School.ttf");
        textView.setTypeface(typeFace);

        getActionBar().hide();

        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
       final String str1=username.getText().toString();
       final String str2= password.getText().toString();
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用账户服务中的登录验证
                //TODO 要开启单独的线程，避免主线程失去响应
                int r = binder.login(str1,str2 );
                switch (r) {
                    case Global.SUCCESS:
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
            }
        });

        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
}
});
        }

}



