package com.socialmap.yy.travelbox;

import android.app.Activity;

import android.content.Intent;

import android.database.Cursor;

import android.os.Bundle;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.Toast;



public class RegisterActivity extends Activity {

    private RegisterHelper dbHelper;

    EditText edtext;

    EditText edpwd;

    EditText edpwd2;

    protected void onCreate(Bundle savedInstanceState)

    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        Button btnsub=(Button)findViewById(R.id.register);

        edtext=(EditText)findViewById(R.id.telnum);

        edpwd=(EditText)findViewById(R.id.password);

        edpwd2=(EditText)findViewById(R.id.password2);
        dbHelper = new RegisterHelper(this, "register.db", null, 1);


        btnsub.setOnClickListener(new View.OnClickListener() {



            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub

                setUser();

            }

        });




    }

    private void setUser()

    {






        if(edtext.getText().toString().length()<=0||edpwd.getText().toString().length()<=0||edpwd2.getText().toString().length()<=0)

        {

            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_LONG).show();

            return;

        }

        if(edtext.getText().toString().length()>0)

        {

            String sql="select * from user where userid=?";

            Cursor cursor=dbHelper.getWritableDatabase().rawQuery(sql, new String[]{edtext.getText().toString()});

            if(cursor.moveToFirst())

            {

                Toast.makeText(this, "用户名已经存在", Toast.LENGTH_LONG).show();

                return;

            }

        }

        if(!edpwd.getText().toString().equals(edpwd2.getText().toString()))

        {

            Toast.makeText(this, "两次输入的密码不同", Toast.LENGTH_LONG).show();

            return;

        }

        if(dbHelper.AddUser(edtext.getText().toString(), edpwd.getText().toString()))

        {

            Toast.makeText(this, "用户注册成功", Toast.LENGTH_LONG).show();

            Intent intent=new Intent();

            intent.setClass(this, MainActivity.class);

            startActivity(intent);

        }

        else

        {

            Toast.makeText(this, "用户注册失败", Toast.LENGTH_LONG).show();

        }

        dbHelper.close();

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

