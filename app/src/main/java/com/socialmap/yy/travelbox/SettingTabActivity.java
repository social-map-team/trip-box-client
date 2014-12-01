package com.socialmap.yy.travelbox;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SettingTabActivity extends Activity implements OnItemClickListener{
    
    private CornerListView cornerListView = null;
    
    private List<Map<String,String>> listData = null;
    private SimpleAdapter adapter = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_setting);
        
        cornerListView = (CornerListView)findViewById(R.id.setting_list);
        setListData();
        
        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.main_tab_setting_list_item , new String[]{"text"}, new int[]{R.id.setting_list_item_text});
        cornerListView.setAdapter(adapter);
        cornerListView.setOnItemClickListener(this);
    }
    

    private void setListData(){
		listData = new ArrayList<Map<String, String>>();

		Map<String,String> map = new HashMap<String, String>();
        map.put("text", "帐号管理");
        listData.add(map);
        
        map = new HashMap<String, String>();
        map.put("text", "消息管理");
        listData.add(map);
        
        map = new HashMap<String, String>();
        map.put("text", "通用设置");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "SOS设置");
        listData.add(map);


        map = new HashMap<String, String>();
        map.put("text", "关于我们");
        listData.add(map);

        map = new HashMap<String, String>();
        map.put("text", "退出登录");
        listData.add(map);


    }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		switch (position) {
		case 0:
			Toast.makeText(SettingTabActivity.this, "帐号管理",Toast.LENGTH_SHORT).show();
			
			break;
		case 1:
			Toast.makeText(SettingTabActivity.this, "消息管理", Toast.LENGTH_SHORT).show();
			
			break;
		case 2:
			Toast.makeText(SettingTabActivity.this, "通用设置", Toast.LENGTH_SHORT).show();
            break;
        case 3:
            Toast.makeText(SettingTabActivity.this, "SOS设置", Toast.LENGTH_SHORT).show();
            break;
        case 4:
            Toast.makeText(SettingTabActivity.this, "关于我们", Toast.LENGTH_SHORT).show();
            break;
        case 5:
            Toast.makeText(SettingTabActivity.this, "退出登录", Toast.LENGTH_SHORT).show();
            break;




		default:
			break;
		}
	}
}