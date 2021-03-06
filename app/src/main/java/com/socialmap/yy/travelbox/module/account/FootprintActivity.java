package com.socialmap.yy.travelbox.module.account;

/**
 * Created by gxyzw_000 on 2014/11/29.
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.History;
import com.socialmap.yy.travelbox.model.HistoryData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 关于格式问题，提几点建议：
 * 1. historyActivity -> HistoryActivity
 * 2. TimelineAdapter 你写成一个外部类，如果这个类仅仅是HistoryActivity使用，那么请写成HistoryActivity的内部类
 * 3. 缩进对齐很重要
 * 4. 没用的TODO删掉
 */

public class FootprintActivity extends Activity {
    public String profilelocal;
    public EditText inputString;
    public EditText inputString_edit;
    public TextView inputLocal;
    public int i;
    private int currentPosition = 0;
    private ListView listView;
    private TimelineAdapter timelineAdapter;
    private List<Map<String, Object>> dataSource;
    private ArrayList<HashMap<String, Object>> historylist = new ArrayList<HashMap<String, Object>>();
    private HistoryData HD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);


        HD = new HistoryData(this);
        historylist = HD.getUserList();

        listView = (ListView) this.findViewById(R.id.listview);
        listView.setDividerHeight(0);
        //     dataSource = getData();
        timelineAdapter = new TimelineAdapter(this, historylist);
        listView.setAdapter(timelineAdapter);


        Intent intent = this.getIntent();

        profilelocal = intent.getStringExtra("profilelocal");
        //   historylocal = profilelocal;

        inputLocal = (TextView) this.findViewById(R.id.local);


        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(actionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 取出所点击的那一项的id
                currentPosition = arg2;

                LayoutInflater inflater = (LayoutInflater) FootprintActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout layout = (LinearLayout) inflater.inflate(
                        R.layout.input_edit, null);
                inputString_edit = (EditText) layout
                        .findViewById(R.id.input_add_string);


                // 弹出的对话框
                new AlertDialog.Builder(FootprintActivity.this)
                    /* 弹出窗口的最上头文字 */
                        .setTitle("修改一条数据")

                    /* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)

                    /* 设置弹出窗口的信息 */
                        .setMessage("请输入修改的内容")

                        .setView(layout)

                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialoginterface, int i) {


                                        // 用户输入修改的足迹内容


                                        // 获取用户添加足迹的输入内容
                                        String str = inputString_edit.getText().toString();
                                        Log.w("yy", str);

                                        // 验证用户输入
                                        if (str == null || str.equals("")) {
                                            Toast.makeText(getApplicationContext(),
                                                    "请输入修改", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {


                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("title", str);
                                            map.put("show_time", "");
                                            map.put("local", "");


                                            dataSource.set(currentPosition, map);
                                            Log.w("yy", String.valueOf(currentPosition));

                                            timelineAdapter.notifyDataSetChanged();

                                            Toast.makeText(
                                                    FootprintActivity.this,
                                                    "修改的数据为:" + str + "",
                                                    Toast.LENGTH_SHORT).show();

                                        }


      /*

                                        // 下面这行是错误的根本：你改变这个新建的list，然后通知listView数据源改变了
                                        // （数据源的list根本没变）
                                        // List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                                       // Map<String, Object> map = new HashMap<String, Object>();

                                        // 验证用户输入

                                        } else {
                                            map.put("title", editStr);
                                            dataSource.set(currentPosition, map);

                                            // 这时真正的数据源已经被修改，通知listView要刷新重绘了
                                            timelineAdapter.notifyDataSetChanged();

                                            Toast.makeText(historyActivity.this,
                                                    "数据修改为:" + editStr + "",
                                                    Toast.LENGTH_SHORT).show();
                                        }
*/
                                    }

                                })

                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    /* 设置跳出窗口的返回事件 */
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        Toast.makeText(FootprintActivity.this,
                                                "取消了修改数据", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        historylist = HD.getUserList();


        timelineAdapter = new TimelineAdapter(this, historylist);
        listView.setAdapter(timelineAdapter);

    }

/*
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "常州恐龙园");
        map.put("show_time", "10.1");
        map.put("local", "常州");
        list.add(map);
        return list;
    }
*/


    //创建actionbar设置一个增加按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.history_menu1, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_add:
                LayoutInflater inflater = (LayoutInflater) FootprintActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(
                        R.layout.input_add, null);
                inputString = (EditText) layout
                        .findViewById(R.id.input_add_string);


                new AlertDialog.Builder(FootprintActivity.this)

                    /* 弹出窗口的最上头文字 */
                        .setTitle("添加一条数据")

                    /* 设置弹出窗口的图式 */
                        .setIcon(android.R.drawable.ic_dialog_info)

                    /* 设置弹出窗口的信息 */
                        .setMessage("请输入添加的内容")

                        .setView(layout)

                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {


                                        //inputLocal.setText(historyActivity.this.profilelocal);
                                        //Log.v("腊肉1",historyActivity.this.profilelocal);

                                        // 获取用户添加足迹的输入内容
                                        String str = inputString.getText().toString();
                                        String str3 = FootprintActivity.this.profilelocal;
                                        //Log.v("腊肉2",str3);
                                        // 验证用户输入
                                        if (str == null || str.equals("")) {
                                            Toast.makeText(getApplicationContext(),
                                                    "添加的内容不能为空", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {


                                            // 和上面一样的错误
                                            // List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd     ");
                                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                            String str2 = formatter.format(curDate);

                                            HashMap<String, Object> map = new HashMap<String, Object>();
                                            map.put("title", str);
                                            map.put("show_time", str2);
                                            map.put("local", str3);


                                            // SharedPreferences sp =historyActivity.this.getSharedPreferences("history", Context.MODE_WORLD_READABLE);


                                            History hi = new History();
                                            hi.setLocal(str);
                                            hi.setShow_time(str2);
                                            hi.setTitle(str3);

                                            HD.addNew(hi);


                                            dataSource.add(0, map);


                                            timelineAdapter.notifyDataSetChanged();

                                            Toast.makeText(
                                                    FootprintActivity.this,
                                                    "添加的数据为:" + str + "",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    /* 设置跳出窗口的返回事件 */
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        Toast.makeText(FootprintActivity.this,
                                                "取消了添加数据", Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



