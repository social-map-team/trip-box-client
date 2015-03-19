package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class AddActivity extends Activity {
    /*
    * 定义显示时间的组件
    */
    private TextView mDateDisplay;
    private Button btnSetDate;
    private Button btnSetTime;
    private Button btnSave;
    private Button btnCancel;
    private EditText txtTitle;
    private EditText txtInfo;
    private CheckBox cbLev;

    // 记录年，月，日
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

//显示提示信息

    public void ShowMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /*
    * 定义时间显示监听器
    */
    private DatePickerDialog.OnDateSetListener mSetDateListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int myear, int monthOfYear,int dayOfMonth) {
// 进行时间显示的更新
            year = myear;
            month = monthOfYear;
            day = dayOfMonth;
// 更新时间的显示
            updateDisplay();
        }
    };

    private TimePickerDialog.OnTimeSetListener mSetTimeListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int mhour, int mminute) {
// 进行时间显示的更新
            hour = mhour;
            minute = mminute;
// 更新时间的显示
            updateDisplay();
        }
    };

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

// 得到组件对象
        mDateDisplay = (TextView) findViewById(R.id.tvDate);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtInfo = (EditText) findViewById(R.id.txtInfo);
        cbLev=(CheckBox)findViewById(R.id.cbLev);

// 为按钮添加监听器
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
// 显示时间对话框
                showDialog(DATE_DIALOG_ID);
            }
        });

//为按钮添加监听器
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//显示时间对话框
                showDialog(TIME_DIALOG_ID);
            }
        });

//为按钮添加监听器
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScheduleLocalActivity.dbHelper.open();
                ContentValues values = new ContentValues(); // 相当于map
                values.put("title", txtTitle.getText().toString());
                values.put("info",  txtInfo.getText().toString());
                values.put("t",  mDateDisplay.getText().toString());
                if (cbLev.isChecked())
                    values.put("level",1);
                else
                    values.put("level",2);

                ScheduleLocalActivity.dbHelper.insert("tItem", values);
                ScheduleLocalActivity.dbHelper.closeConnection();
                ShowMsg("日程保存成功！");

                Intent intent =new Intent();
                intent.setClass(AddActivity.this,ScheduleLocalActivity.class);
                startActivity(intent);
                AddActivity.this.finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(AddActivity.this,MainActivity.class);
                startActivity(intent);
                AddActivity.this.finish();
            }
        });

// 得到当前的时间
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);

// 更新现有时间
        updateDisplay();
    }

    /**
     * 当显示时间窗口被创建时调用
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mSetDateListener, year, month,day);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mSetTimeListener, hour, minute,true);
        }
        return null;
    }

    /**
     * 更新时间显示器
     */
    private void updateDisplay() {
        mDateDisplay.setText(new StringBuilder()// Month is 0 based so add 1
                .append(month + 1).append("-").append(day).append("-").append(year).append(" ").append(hour).append(":").append(minute));
    }


}
