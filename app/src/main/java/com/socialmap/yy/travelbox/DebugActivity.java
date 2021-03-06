package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.data.DBHelper;
import com.socialmap.yy.travelbox.module.account.RegisterActivity;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by yy on 3/23/15.
 */
public class DebugActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViewById(R.id.reset_local_schedule_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getBaseContext());
                dbHelper.open();

                String deleteSql = "drop table if exists tItem ";
                dbHelper.execSQL(deleteSql);

                // id：自动递增
                // title: 标题 概要
                // info：详细内容
                // t: 日期时间
                if (!dbHelper.isTableExist("tItem"))   //判断表是否存在
                {
                    try {
                        String sql = IOUtils.toString(getAssets().open("create_local_schedule_table.sql"));
                        dbHelper.execSQL(sql);

                        // 插入默认数据
                        String format = "INSERT INTO tItem(t, title, info, level) VALUES('%s', '%s', '%s', %s)";
                        InputStream is = getAssets().open("sample_local_schedules.txt");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String[] data = new String[4];
                        boolean hasdata = false;
                        int currentIndex = 0;
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.isEmpty()) {
                                if (hasdata) {
                                    // insert
                                    dbHelper.execSQL(String.format(format, data[0], data[1], data[2], data[3]));
                                    hasdata = false;
                                    data[0] = data[1] = data[2] = data[3] = null;
                                    currentIndex = 0;
                                }
                            } else {
                                hasdata = true;
                                if (line.startsWith("时间：")) {
                                    data[0] = line.substring(3);
                                    currentIndex = 0;
                                } else if (line.startsWith("标题：")) {
                                    data[1] = line.substring(3);
                                    currentIndex = 1;
                                } else if (line.startsWith("简介：")) {
                                    data[2] = line.substring(3);
                                    currentIndex = 2;
                                } else if (line.startsWith("星等：")) {
                                    data[3] = line.substring(3);
                                    currentIndex = 3;
                                } else {
                                    data[currentIndex] += line;
                                }
                            }
                        }
                        if (hasdata) {
                            dbHelper.execSQL(String.format(format, data[0], data[1], data[2], data[3]));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dbHelper.closeConnection();
                Toast.makeText(getApplicationContext(), "本地日程数据库重置完毕", Toast.LENGTH_LONG).show();
            }
        });


        findViewById(R.id.start_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((App)getApplication()).locationClient.start();
                updateLocStatus();
            }
        });

        findViewById(R.id.stop_loc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((App)getApplication()).locationClient.stop();
                updateLocStatus();
            }
        });

        updateLocStatus();

        findViewById(R.id.quick_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DebugActivity.this, RegisterActivity.class));
            }
        });


    }

    private void updateLocStatus(){
        ((TextView)findViewById(R.id.loc_status)).setText(
                ((App)getApplication()).locationClient.isStarted()?
                        "定位服务已开启":"定位服务未开启"
        );
    }
}
