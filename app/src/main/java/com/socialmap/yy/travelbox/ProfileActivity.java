package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by yy on 7/24/14.
 */
public class ProfileActivity extends Activity {
    ImageView button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ListView list = (ListView) findViewById(R.id.list);
        button = (ImageView)findViewById(R.id.avatar_wrapper);
        button.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转。       ImageView 里面的图片需要添加为用户头像
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        String[] profile = new String[]{
                "用户名：bitdancer",
                "身份证号：XXXXXXXXXXXXXXXXXX",
                "真实姓名：杨阳",
                "生日：1992年3月19日",
                "联系电话: +86XXXXXXXXXXX",
                "联系邮箱: oplogobit@gmail.com"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, profile);
        list.setAdapter(adapter);
    }
}
