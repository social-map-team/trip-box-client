package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Setting1Activity extends Activity implements OnItemClickListener {
    private android.widget.ListView ListView = null;

    private List<Map<String, String>> listData = null;
    private SimpleAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_setting);

        ListView = (ListView) findViewById(R.id.setting_list);
        setListData();

        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.main_tab_setting_list_item, new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        ListView.setAdapter(adapter);
        ListView.setOnItemClickListener(this);
    }


    private void setListData() {
        listData = new ArrayList<Map<String, String>>();

        Map<String, String> map = new HashMap<String, String>();
        map.put("text", "修改资料");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "修改密码");
        listData.add(map);
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position) {
            case 0:
                Toast.makeText(Setting1Activity.this, "修改资料", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(Setting1Activity.this, "修改密码", Toast.LENGTH_SHORT).show();
                break;


        }
    }


}












