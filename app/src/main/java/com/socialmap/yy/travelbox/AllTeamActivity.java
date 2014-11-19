package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by LZY on 2014/11/19.  对应“团队”界面 开发组21号拿到UI设计，根据对应功能进行扩充
 */
public class AllTeamActivity extends Activity {
    ImageButton backbutton;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_team);

        backbutton = (ImageButton)findViewById(R.id.backbutton);
        listView = (ListView)findViewById(R.id.listView);
        backbutton.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(AllTeamActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        String[] AllTeam = new String[]{

                "名称：",
                "发起人：",
                "目的地：",
                "日期：",
                "状态：",
                "日程：",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllTeam);
        listView.setAdapter(adapter);

    }
}