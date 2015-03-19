package com.socialmap.yy.travelbox;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.chat.exception.XXAdressMalformedException;
import com.socialmap.yy.travelbox.chat.service.IConnectionStatusCallback;
import com.socialmap.yy.travelbox.chat.service.XXService;
import com.socialmap.yy.travelbox.chat.util.DialogUtil;
import com.socialmap.yy.travelbox.chat.util.L;
import com.socialmap.yy.travelbox.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.chat.util.PreferenceUtils;
import com.socialmap.yy.travelbox.chat.util.T;
import com.socialmap.yy.travelbox.chat.util.XMPPHelper;
import com.socialmap.yy.travelbox.chat.view.ChangeLog;
import com.socialmap.yy.travelbox.service.AccountService;
import com.socialmap.yy.travelbox.utils.Global;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity  extends FragmentActivity implements
        IConnectionStatusCallback, TextWatcher {

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
        //登录tbs服务器
        //绑定账户服务
        bindService(new Intent("com.socialmap.yy.travelbox.ACCOUNT_SERVICE"),
                conn,
                Service.BIND_AUTO_CREATE);

        TextView textView = (TextView) findViewById(R.id.welcome_title);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),"fonts/Sketch Gothic School.ttf");
        textView.setTypeface(typeFace);

      //  getActionBar().hide();

       final EditText username = (EditText) findViewById(R.id.username);
       final EditText password = (EditText) findViewById(R.id.password);
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


        //登录聊天服务器

        startService(new Intent(LoginActivity.this, XXService.class));
        bindXMPPService();
        initView();


    }



//登录聊天服务器


        public static final String LOGIN_ACTION = "com.socialmap.yy.travelbox.action.LOGIN";
        private static final int LOGIN_OUT_TIME = 0;
        private Button mLoginBtn;
        private EditText mAccountEt;
        private EditText mPasswordEt;
        private CheckBox mAutoSavePasswordCK;
        private CheckBox mHideLoginCK;

        private XXService mXxService;
        private Dialog mLoginDialog;
        private ConnectionOutTimeProcess mLoginOutTimeProcess;
        private String mAccount;
        private String mPassword;

        private Animation mTipsAnimation;

        private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case LOGIN_OUT_TIME:
                        if (mLoginOutTimeProcess != null
                                && mLoginOutTimeProcess.running)
                            mLoginOutTimeProcess.stop();
                        if (mLoginDialog != null && mLoginDialog.isShowing())
                            mLoginDialog.dismiss();
                        T.showShort(LoginActivity.this, R.string.timeout_try_again);
                        break;

                    default:
                        break;
                }
            }

        };
        ServiceConnection mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mXxService = ((XXService.XXBinder) service).getService();
                mXxService.registerConnectionStatusCallback(LoginActivity.this);
                // 开始连接xmpp服务器
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mXxService.unRegisterConnectionStatusCallback();
                mXxService = null;
            }

        };



        @Override
        protected void onResume() {
        super.onResume();

        ChangeLog cl = new ChangeLog(this);
        if (cl.firstRun()) {
            cl.getFullLogDialog().show();
        }
    }

        @Override
        protected void onPause() {
        super.onPause();

    }

        @Override
        protected void onDestroy() {
        super.onDestroy();
        unbindXMPPService();
        if (mLoginOutTimeProcess != null) {
            mLoginOutTimeProcess.stop();
            mLoginOutTimeProcess = null;
        }
    }

    private void initView() {
        mTipsAnimation = AnimationUtils.loadAnimation(this, R.anim.connection);
        mAutoSavePasswordCK = (CheckBox) findViewById(R.id.auto_save_password);
        mHideLoginCK = (CheckBox) findViewById(R.id.hide_login);



        mAccountEt = (EditText) findViewById(R.id.username);
        mPasswordEt = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login);
        String account = PreferenceUtils.getPrefString(this,
                PreferenceConstants.ACCOUNT, "");
        String password = PreferenceUtils.getPrefString(this,
                PreferenceConstants.PASSWORD, "");
        if (!TextUtils.isEmpty(account))
            mAccountEt.setText(account);
        if (!TextUtils.isEmpty(password))
            mPasswordEt.setText(password);
        mAccountEt.addTextChangedListener(this);
        mLoginDialog = DialogUtil.getLoginDialog(this);
        mLoginOutTimeProcess = new ConnectionOutTimeProcess();
    }

    public void onLoginClick(View v) {
        mAccount = mAccountEt.getText().toString();
        mAccount = splitAndSaveServer(mAccount);
        mPassword = mPasswordEt.getText().toString();
        if (TextUtils.isEmpty(mAccount)) {
            T.showShort(this, R.string.null_account_prompt);
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            T.showShort(this, R.string.password_input_prompt);
            return;
        }
        if (mLoginOutTimeProcess != null && !mLoginOutTimeProcess.running)
            mLoginOutTimeProcess.start();
        if (mLoginDialog != null && !mLoginDialog.isShowing())
            mLoginDialog.show();
        if (mXxService != null) {
            mXxService.Login(mAccount, mPassword);
        }
    }

    private String splitAndSaveServer(String account) {
        if (!account.contains("@"))
            return account;
        String customServer = PreferenceUtils.getPrefString(this,
                PreferenceConstants.CUSTOM_SERVER, "");
        String[] res = account.split("@");
        String userName = res[0];
        String server = res[1];
        // check for gmail.com and other google hosted jabber accounts
        if ( PreferenceConstants.GMAIL_SERVER.equals(customServer)) {
            // work around for gmail's incompatible jabber implementation:
            // send the whole JID as the login, connect to talk.google.com
            userName = account;

        }
        PreferenceUtils.setPrefString(this, PreferenceConstants.Server, server);
        return userName;
    }

    private void unbindXMPPService() {
        try {
            unbindService(mServiceConnection);
            L.i(LoginActivity.class, "[SERVICE] Unbind");
        } catch (IllegalArgumentException e) {
            L.e(LoginActivity.class, "Service wasn't bound!");
        }
    }

    private void bindXMPPService() {
        L.i(LoginActivity.class, "[SERVICE] Unbind");
        Intent mServiceIntent = new Intent(this, XXService.class);
        mServiceIntent.setAction(LOGIN_ACTION);
        bindService(mServiceIntent, mServiceConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            XMPPHelper.verifyJabberID(s);
            mLoginBtn.setEnabled(true);
            mAccountEt.setTextColor(Color.parseColor("#ff333333"));
        } catch (XXAdressMalformedException e) {
            mLoginBtn.setEnabled(false);
            mAccountEt.setTextColor(Color.RED);
        }
    }

    private void save2Preferences() {
        boolean isAutoSavePassword = mAutoSavePasswordCK.isChecked();
        boolean isHideLogin = mHideLoginCK.isChecked();
        PreferenceUtils.setPrefString(this, PreferenceConstants.ACCOUNT,
                mAccount);// 帐号是一直保存的
        if (isAutoSavePassword)
            PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
                    mPassword);
        else
            PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD,
                    "");

        if (isHideLogin)
            PreferenceUtils.setPrefString(this,
                    PreferenceConstants.STATUS_MODE, PreferenceConstants.XA);
        else
            PreferenceUtils.setPrefString(this,
                    PreferenceConstants.STATUS_MODE,
                    PreferenceConstants.AVAILABLE);
    }

    // 登录超时处理线程
    class ConnectionOutTimeProcess implements Runnable {
        public boolean running = false;
        private long startTime = 0L;
        private Thread thread = null;

        ConnectionOutTimeProcess() {
        }

        public void run() {
            while (true) {
                if (!this.running)
                    return;
                if (System.currentTimeMillis() - this.startTime > 20 * 1000L) {
                    mHandler.sendEmptyMessage(LOGIN_OUT_TIME);
                }
                try {
                    Thread.sleep(10L);
                } catch (Exception localException) {
                }
            }
        }

        public void start() {
            try {
                this.thread = new Thread(this);
                this.running = true;
                this.startTime = System.currentTimeMillis();
                this.thread.start();
            } finally {
            }
        }

        public void stop() {
            try {
                this.running = false;
                this.thread = null;
                this.startTime = 0L;
            } finally {
            }
        }
    }

    @Override
    public void connectionStatusChanged(int connectedState, String reason) {
        // TODO Auto-generated method stub
        if (mLoginDialog != null && mLoginDialog.isShowing())
            mLoginDialog.dismiss();
        if (mLoginOutTimeProcess != null && mLoginOutTimeProcess.running) {
            mLoginOutTimeProcess.stop();
            mLoginOutTimeProcess = null;
        }
        if (connectedState == XXService.CONNECTED) {
            save2Preferences();
            startActivity(new Intent(this, com.socialmap.yy.travelbox.chat.activity.MainActivity.class));
            finish();
        } else if (connectedState == XXService.DISCONNECTED)
            T.showLong(LoginActivity.this, getString(R.string.request_failed)
                    + reason);
    }
























}
