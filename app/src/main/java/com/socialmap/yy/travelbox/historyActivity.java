package com.socialmap.yy.travelbox;

/**
 * Created by gxyzw_000 on 2014/11/29.
 */


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.adpater.TimelineAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class historyActivity extends Activity {
        int currentPosition = 0;
        private ListView listView;
    List<String> data ;
    private TimelineAdapter timelineAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.history_list);

            listView = (ListView) this.findViewById(R.id.listview);
            listView.setDividerHeight(0);
            timelineAdapter = new TimelineAdapter(this, getData());
            listView.setAdapter(timelineAdapter);

            ActionBar actionBar = this.getActionBar();
            actionBar.setDisplayOptions(actionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);




            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                @Override
                                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                                        long arg3) {
                                                    // TODO Auto-generated method stub
                                                    // 取出所点击的那一项的id
                                                     currentPosition= arg2;

                                                    LayoutInflater inflater = (LayoutInflater) historyActivity.this
                                                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                    final LinearLayout layout = (LinearLayout) inflater.inflate(
                                                            R.layout.input_edit, null);



                                                    // 弹出的对话框

                                                    new AlertDialog.Builder(historyActivity.this)
                    /* 弹出窗口的最上头文字 */
                                                            .setTitle("修改一条数据")
                    /* 设置弹出窗口的图式 */
                                                            .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                                                            .setMessage("请输入修改的内容")
                                                            .setView(layout)
                                                            .setPositiveButton("确定",
                                                                    new DialogInterface.OnClickListener() {
                                                                        public void onClick(
                                                                                DialogInterface dialoginterface, int i) {



                                                                            EditText inputEditString = (EditText) layout
                                                                                    .findViewById(R.id.input_edit_string);

                                                                            String editStr = inputEditString.getText()
                                                                                    .toString();
                                                                            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                                                            Map<String, Object> map = new HashMap<String, Object>();

                                                                            if (editStr == null)
                                                                                    {

                                                                                Toast.makeText(getApplicationContext(),
                                                                                        "请输入修改", Toast.LENGTH_SHORT)
                                                                                        .show();
                                                                            } else {



                                                                                    map.put("title", editStr);

                                                                                    list.set(currentPosition,map);
                                                                                    timelineAdapter
                                                                                            .notifyDataSetChanged();

                                                                                    Toast.makeText(
                                                                                            historyActivity.this,
                                                                                            "数据修改为:" + editStr + "",
                                                                                            Toast.LENGTH_SHORT).show();


                                                                            }

                                                                        }
                                                                    })
                                                            .setNegativeButton("取消",
                                                                    new DialogInterface.OnClickListener() { /* 设置跳出窗口的返回事件 */
                                                                        public void onClick(
                                                                                DialogInterface dialoginterface, int i) {
                                                                            Toast.makeText(historyActivity.this,
                                                                                    "取消了修改数据", Toast.LENGTH_SHORT)
                                                                                    .show();

                                                                        }
                                                                    }).show();

                                                }





        });



                                            }


            private List<Map<String, Object>> getData() {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", "常州恐龙园");
            map.put("show_time","10.1");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "春秋淹城");
                map.put("show_time","10.1");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "天目山");
                map.put("show_time","10.2");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "嬉戏谷");
                map.put("show_time","10.3");
            list.add(map);


            map = new HashMap<String, Object>();
            map.put("title", "狮子林");
                map.put("show_time","10.4");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "拙政园");
                map.put("show_time","10.4");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "虎丘");
                map.put("show_time","10.5");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "苏堤");
                map.put("show_time","10.7");
            list.add(map);

            map = new HashMap<String, Object>();
            map.put("title", "周庄");
                map.put("show_time","10.8");
            list.add(map);

            return list;



        }


//创建actionbar设置一个增加按钮
    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);



        getMenuInflater().inflate(R.menu.history_menu1, menu);


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.history_add:
                LayoutInflater inflater = (LayoutInflater) historyActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout layout = (LinearLayout) inflater.inflate(
                        R.layout.input_add, null);
                new AlertDialog.Builder(historyActivity.this)
                    /* 弹出窗口的最上头文字 */
                        .setTitle("添加一条数据")
                    /* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                        .setMessage("请输入添加的内容")
                        .setView(layout)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {

                                        EditText inputStringr = (EditText) layout
                                                .findViewById(R.id.input_add_string);

                                        String str = inputStringr.getText()
                                                .toString();


                                        if (str == null || str.equals("")) {

                                            Toast.makeText(getApplicationContext(),
                                                    "添加的内容不能为空", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {

                                            if (str.contains(" ")) {


                                                String[] temp = str.split(" ");
                                                String str1 = temp[0];
                                                String str2 = temp[1];

                                                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                                HashMap<String, Object> map = new HashMap<String, Object>();
                                                map.put("title", str1);
                                                map.put("show_time", str2);
                                                list.add(0, map);
                                                // 如果在前面添加一条数据添加
                                                // listItem.add(map);
                                                timelineAdapter.notifyDataSetChanged();
                                                Toast.makeText(
                                                        historyActivity.this,
                                                        "添加的数据为:" + str + "",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(historyActivity.this,
                                                        "输入格式应该为“地点+空格+时间”", Toast.LENGTH_SHORT)
                                                        .show();
                                            }


                                        }

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() { /* 设置跳出窗口的返回事件 */
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        Toast.makeText(historyActivity.this,
                                                "取消了添加数据", Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                }).show();




                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }










    }


