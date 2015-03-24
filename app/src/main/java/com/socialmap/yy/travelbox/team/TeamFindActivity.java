package com.socialmap.yy.travelbox.team;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.adpater.FindTeamAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeamFindActivity extends Activity {

    private FindTeamAdapter findTeamAdapter;
    private List<Map<String, Object>> dataSource;
    private ListView listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_team_list);

        listView = (ListView) this.findViewById(R.id.listview);
        listView.setDividerHeight(0);
        dataSource = getData();
        findTeamAdapter = new FindTeamAdapter(this, dataSource);
        listView.setAdapter(findTeamAdapter);
    }










    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "西藏之旅");
        map.put("member", "12/20");
        map.put("introduce", "一起去西藏吧");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "西藏之旅");
        map.put("member", "12/20");
        map.put("introduce", "一起去西藏吧");
        list.add(map);


        return list;
    }





}
