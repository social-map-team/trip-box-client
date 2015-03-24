package com.socialmap.yy.travelbox.account;


import com.socialmap.yy.travelbox.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyData{
    public static  List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("title", "当地美食");
        map.put("img", R.drawable.food);

        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "风景名胜");
        map.put("img", R.drawable.scene);

        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "历史文化");
        map.put("img", R.drawable.culture);

        list.add(map);


        map = new HashMap<String, Object>();
        map.put("title", "民俗工艺");
        map.put("img", R.drawable.craft);

        list.add(map);



        map = new HashMap<String, Object>();
        map.put("title", "生活服务");
        map.put("img", R.drawable.life);

        list.add(map);


        map = new HashMap<String, Object>();
        map.put("title", "酒店住宿");
        map.put("img", R.drawable.hotel);

        list.add(map);




        return list;
    }
}