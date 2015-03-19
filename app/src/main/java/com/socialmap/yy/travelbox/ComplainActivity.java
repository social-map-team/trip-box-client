package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.socialmap.yy.travelbox.chat.util.T;


public class ComplainActivity extends Activity{
        private EditText mFeedBackEt;
        private Button mSendBtn;
        private  Button cancel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.feed_back_view);
            mFeedBackEt = (EditText) findViewById(R.id.fee_back_edit);
            mSendBtn = (Button) findViewById(R.id.feed_back_btn);
            mSendBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String content = mFeedBackEt.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "旅游盒子 - 信息反馈");
                        intent.putExtra(Intent.EXTRA_TEXT, content);
                        intent.setData(Uri.parse("shuwangyi@yeah.net"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ComplainActivity.this.startActivity(intent);
                        Toast.makeText(v.getContext(), R.string.complain_send_success, Toast.LENGTH_LONG).show();
                    } else {
                        T.showShort(ComplainActivity.this, "请输入一点点内容嘛！");
                    }
                }
            });




        }
    }



