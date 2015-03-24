package com.socialmap.yy.travelbox.module.settings;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.socialmap.yy.travelbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gxyzw_000 on 2015/3/4.
 */
public class Setting32Fragment extends ListFragment {

//todo 导入通讯录

    private ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.listview);
        //listView = (ListView)findViewById(R.id.lv);
        //listView = new ListView(this);

        Cursor cursor = getActivity().getContentResolver().query(Contacts.People.CONTENT_URI,
                null,null,null,null);  //TODO 获得联系人信息，这块获取现在只能获得姓名，但是号码获取方式不确定
        getActivity().startManagingCursor(cursor);
        ListAdapter listAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.phone_listview_item, cursor,
                new String[]{Contacts.People.NAME, Contacts.People.NUMBER},//TODO 问题对应上面的TODO，前者是获取姓名的数据，后面是号码，但是第28行缺少获取号码的方式。
                new  int[]{R.id.name, R.id.num});
        setListAdapter(listAdapter);

        //setContentView(listView);
        // SimpleAdapter adapter = new SimpleAdapter(this,R.layout.activity_main,new String[]{"title","info"},
        //new int[]{R.id.title,R.id.info});

        //setListAdapter(adapter);
        //TODO listview点击事件
        /*listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTitle("123");
            }
        });*/
    }
    //TODO 数据引用方法
    private List<Map<String,Object>> getData(){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<String, Object>();


        map.put("info","123");
        list.add(map);
        return list;
    }
}
