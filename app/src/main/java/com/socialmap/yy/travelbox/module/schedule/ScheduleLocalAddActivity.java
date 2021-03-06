package com.socialmap.yy.travelbox.module.schedule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;

import java.util.Calendar;

public class ScheduleLocalAddActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    private int year;
    private int month;
    private int day;
    private DatePickerDialog.OnDateSetListener mSetDateListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int myear, int monthOfYear, int dayOfMonth) {
            year = myear;
            month = monthOfYear;
            day = dayOfMonth;
            updateDisplay();
        }
    };


    private int hour;
    private int minute;
    private TimePickerDialog.OnTimeSetListener mSetTimeListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int mhour, int mminute) {
            hour = mhour;
            minute = mminute;
            updateDisplay();
        }
    };

    private TextView time;
    private TextView info;
    private TextView title;
    private RatingBar level;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_local_add);

        time = (TextView) findViewById(R.id.time);
        info = (TextView) findViewById(R.id.info);
        title = (TextView) findViewById(R.id.title);
        level = (RatingBar) findViewById(R.id.level);

        // 设置时间按钮
        findViewById(R.id.set_time).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // 设置日期按钮
        findViewById(R.id.set_date).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        // 保存按钮
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScheduleLocalActivity.dbHelper.open();
                ContentValues values = new ContentValues(); // 相当于map
                values.put("title", title.getText().toString());
                values.put("info", info.getText().toString());
                values.put("t", time.getText().toString());
                values.put("level", level.getRating());
                ScheduleLocalActivity.dbHelper.insert("tItem", values);
                ScheduleLocalActivity.dbHelper.closeConnection();

                Toast.makeText(getApplicationContext(), "新的日程项已经添加", Toast.LENGTH_LONG).show();

                startActivity(new Intent(ScheduleLocalAddActivity.this, ScheduleLocalActivity.class));
                finish();
            }
        });

        // 取消按钮
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ScheduleLocalAddActivity.this, ScheduleLocalActivity.class));
                finish();
            }
        });

        // 得到当前的时间
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // 更新现有时间
        updateDisplay();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mSetDateListener, year, month, day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mSetTimeListener, hour, minute, true);
        }
        return null;
    }

    private void updateDisplay() {
        time.setText(new StringBuilder()// Month is 0 based so add 1
                .append(year).append("-").append(month + 1).append("-").append(day).append(" ")
                .append(hour).append(":").append(minute));
    }
}
