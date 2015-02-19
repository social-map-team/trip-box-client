package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Setting2Activity  extends Activity implements AdapterView.OnItemClickListener {


        private android.widget.ListView ListView = null;

        private List<Map<String, String>> listData = null;
        private SimpleAdapter adapter = null;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_tab_setting);

            ListView = (android.widget.ListView) findViewById(R.id.setting_list);
            setListData();

            adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.main_tab_setting_list_item, new String[]{"text"}, new int[]{R.id.setting_list_item_text});
            ListView.setAdapter(adapter);
            ListView.setOnItemClickListener(this);
        }


        private void setListData() {
            listData = new ArrayList<Map<String, String>>();

            Map<String, String> map = new HashMap<String, String>();
            map.put("text", "开启信息推送");
            listData.add(map);

            map = new HashMap<String, String>();
            map.put("text", "消息免打扰");
            listData.add(map);


            map = new HashMap<String, String>();
            map.put("text", "声音开启");
            listData.add(map);

            map = new HashMap<String, String>();
            map.put("text", "震动开启");
            listData.add(map);










        }


        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            switch (position) {
                case 0:
                    Toast.makeText(Setting2Activity.this, "已开启信息推送", Toast.LENGTH_SHORT).show();
                    break;



            }
        }


    }



