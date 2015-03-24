package com.socialmap.yy.travelbox.module.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.socialmap.yy.travelbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Setting4Activity  extends Activity implements AdapterView.OnItemClickListener {


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
        map.put("text", "语言");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "字体大小");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "主题");
        listData.add(map);


        map = new HashMap<String, String>();
        map.put("text", "清理缓存");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "清空历史纪录");
        listData.add(map);












    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;


        }
    }


}
