package com.socialmap.yy.travelbox.module.account;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.module.main.MainActivity;
import com.socialmap.yy.travelbox.utils.TbsClient;


/**
 * 注册分两步
 * 第一步，用户输入手机后，确认没有被注册，然后服务器会给该号码发送验证短信
 * 为了调试方便，验证码固定位1234
 * 当用户正确输入了验证码以后，便可以设置密码，完成账户注册
 */
public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getFragmentManager().beginTransaction().replace(R.id.container, new Step1()).commit();
    }

    public static class Step1 extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_register_step1, container, false);
            final LinearLayout vcodeWrapper = (LinearLayout) root.findViewById(R.id.vcode_wrapper);
            final EditText phone = (EditText) root.findViewById(R.id.phone);
            final EditText vcode = (EditText) root.findViewById(R.id.vcode);
            final Button next = (Button) root.findViewById(R.id.next);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vcodeWrapper.getVisibility() == View.GONE) {
                        // 当用户输入手机号后，第一次点击注册按钮
                        // 检查该手机号有没有被注册
                        TbsClient.getInstance()
                                .request("/check/existence", "get", "phone", phone.getText())
                                .execute(new TbsClient.Callback() {
                                    @Override
                                    public void onFinished(TbsClient.ServerResponse response) {
                                        if (response.getBoolean()) {
                                            // 电话号码已存在
                                            // FIXME 奇怪的错误：错误提示框内不显示文字
                                            phone.setError(getString(R.string.err_phone_exist));
                                            Toast.makeText(getActivity(), "该号码已被注册，由于上面提示框内不显示文字，只好弹个Toast", Toast.LENGTH_LONG).show();
                                        } else {
                                            // 电话号码不存在
                                            TbsClient.getInstance()
                                                    .request("/register/vcode", "get")
                                                    .execute(new TbsClient.Callback() {
                                                        @Override
                                                        public void onFinished(TbsClient.ServerResponse response) {
                                                            if (response.getBoolean()) {
                                                                // 服务器已成功发送验证码
                                                                vcodeWrapper.setVisibility(View.VISIBLE);
                                                                Toast.makeText(getActivity(), "验证码已发送，请注意查收", Toast.LENGTH_LONG).show();
                                                            } else {
                                                                // TODO 服务器发送验证码失败
                                                                Toast.makeText(getActivity(), "服务器验证码发送失败", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    } else {
                        // 检查输入的验证码是否正确
                        TbsClient.getInstance()
                                .request("/register/vcode", "post",
                                        "vcode", vcode.getText()
                                )
                                .execute(new TbsClient.Callback() {
                                    @Override
                                    public void onFinished(TbsClient.ServerResponse response) {
                                        if (response.getBoolean()) {
                                            // 验证码正确，进入下一步：设置密码
                                            Fragment step2 = new Step2();
                                            Bundle args = new Bundle();
                                            args.putString("phone", phone.getText().toString());
                                            step2.setArguments(args);

                                            getFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.container, step2)
                                                    .commit();
                                        } else {
                                            // 验证码错误
                                            vcode.setError(getString(R.string.err_vcode_wrong));
                                        }
                                    }
                                });
                    }
                }
            });
            return root;
        }
    }

    public static class Step2 extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_register_step2, container, false);
            final EditText password = (EditText) root.findViewById(R.id.password);
            final Switch show = (Switch) root.findViewById(R.id.show);
            final Button register = (Button) root.findViewById(R.id.register);
            show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    } else {
                        password.setInputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone = getArguments().getString("phone", "");
                    TbsClient.getInstance()
                            .request("/register", "post",
                                    "phone", phone,
                                    "password", password.getText(),
                                    "username", phone
                            )
                            .execute(new TbsClient.Callback() {
                                @Override
                                public void onFinished(TbsClient.ServerResponse response) {
                                    if (response.is2xx()) {
                                        // 注册成功, 跳转到登陆界面
                                        Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getActivity(), LoginActivity.class));
                                        getActivity().finish();
                                    } else {
                                        // TODO 注册失败
                                        Toast.makeText(getActivity(), "注册失败，返回主界面", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().finish();
                                    }
                                }
                            });
                }
            });
            return root;
        }
    }
}








































/*
public class RegisterActivity extends Activity implements StandardRequest, StandardResponse {
    private EditText email;
    private CheckEmailAvailableTask emailTask = new CheckEmailAvailableTask();

    private RegisterHelper dbHelper;

    private void setEmailError(String err) {
        email.setError(err);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new RegisterHelper(this, "register.db", null, 1);


        email = (EditText) findViewById(R.id.email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailText = email.getText().toString();
                    if (emailTask.getStatus() != AsyncTask.Status.FINISHED) {
                        emailTask.cancel(true);
                        emailTask = new CheckEmailAvailableTask();
                    }
                    emailTask.execute(emailText);
                }
            }
        });
        final EditText password = (EditText) findViewById(R.id.password);
        Switch toggle = (Switch) findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(RegisterActivity.this, "checked", Toast.LENGTH_SHORT).show();
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    Toast.makeText(RegisterActivity.this, "not checked", Toast.LENGTH_SHORT).show();
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();



                 SQLiteDatabase db = dbHelper.getWritableDatabase();
                 ContentValues values = new ContentValues();// 开始组装第一条数据
                 values.put("num", emailText);
                 values.put("pass", passwordText);
                 db.insert("Register", null, values); // 插入第一条数据
                 values.clear();








                Toast.makeText(RegisterActivity.this, "You registered with ID " + emailText + "and password " + passwordText, Toast.LENGTH_LONG).show();
            }
        });





        }














//TODO 查emil存在

    private class CheckEmailAvailableTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Socket socket;
            try {
                socket = new Socket("192.168.1.201", 37373);
                int op = OP_CHECK_EMAIL_EXISTENCE;
                OutputStream os = socket.getOutputStream();
                os.write(op);
                os.write(op >> 8);
                byte[] emailByteArray = params[0].getBytes("UTF-8");
                os.write(emailByteArray.length);
                os.write(emailByteArray);
                InputStream is = socket.getInputStream();
                int r = is.read();
                socket.close();
                if (r == FAIL) {
                    setEmailError(getString(R.string.register_error_email_registered));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/

