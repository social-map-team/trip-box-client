package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yy on 8/7/14.
 */
public class RegisterActivity extends Activity implements StandardRequest, StandardResponse {
    private EditText email;
    private CheckEmailAvailableTask emailTask = new CheckEmailAvailableTask();

    private void setEmailError(String err) {
        email.setError(err);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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
                Toast.makeText(RegisterActivity.this, "You registered with emial " + emailText + "and password " + passwordText, Toast.LENGTH_LONG).show();
            }
        });
    }

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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
