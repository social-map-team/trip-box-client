package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by yy on 8/3/14.
 */
public class MessageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView list = (ListView) findViewById(R.id.list);
        BaseAdapter adapter = new BaseAdapter() {
            private LayoutInflater inflater = getLayoutInflater();

            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = inflater.inflate(R.layout.activity_message_list_item, null);
                }

                return convertView;
            }
        };
        list.setAdapter(adapter);
    }
}
