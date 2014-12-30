package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;





/**
 * Created by gxyzw_000 on 2014/12/1.
 */
public class TeamGatherActivity extends Activity {


        private EditText showDate = null;
        private Button pickDate = null;
        private EditText showTime = null;
        private Button pickTime = null;

        private static final int SHOW_DATAPICK = 0;
        private static final int DATE_DIALOG_ID = 1;
        private static final int SHOW_TIMEPICK = 2;
        private static final int TIME_DIALOG_ID = 3;

        private int mYear;
        private int mMonth;
        private int mDay;
        private int mHour;
        private int mMinute;



        private Boolean isEmpty = true;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_team_gather);

            initializeViews();

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            setDateTime();
            setTimeOfDay();





        Button location_button = (Button)findViewById(R.id.location_button);
            location_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TeamGatherActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
//地点button

            Button start_button = (Button)findViewById(R.id.start_button);
            start_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(TeamGatherActivity.this,"开始集合",Toast.LENGTH_SHORT).show();
                }
            });


            EditText location_text = (EditText)this.findViewById(R.id.location_text);
//监听控件的焦点改变事件
            location_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View arg0, boolean arg1) {
// TODO Auto-generated method stub
//获取触发事件的EditText
                    EditText clickEditText = (EditText)arg0;
//如果失去焦点
                    if(arg1 == false)
                    {
//获取当前文本
                        String text =clickEditText.getText().toString().trim();
//如果的确人为输入过内容
                        if(text.length()>0
                                &text.equals("请输入集合地点")== false)
                        {
                            isEmpty = false;
                            clickEditText.setTextColor(Color.BLACK);
                            clickEditText.setText(text);
                        }
                        else
                        {
                            clickEditText.setText("请输入集合地点");
                            clickEditText.setTextColor(Color.GRAY);
                            isEmpty = true;
                        }
                    }
//如果获得焦点
                    else
                    {
                        clickEditText.setTextColor(Color.BLACK);
//如果处于未编辑状态，则清空“请输入您的名字”这几个字
                        if(isEmpty == true)
                        {
                            clickEditText.setText("");
                        }
                    }
                }
            });
//监听控件有新字符输入
            location_text.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
// TODO Auto-generated method stub
//获取触发事件的EditText
                    EditText clickEditText = (EditText)arg0;
//获取当前文本
                    String text =clickEditText.getText().toString().trim();
                    if(text.length()==20)
                    {
//提示用户
                        Toast toast = Toast.makeText(TeamGatherActivity.this, "最大长度为20个字符", Toast.LENGTH_SHORT);
                        toast.setGravity(0, 0, 0);
                        toast.show();
                    }
                    return false;
                }
            });




}

        /**
         * 初始化控件和UI视图
         */
        private void initializeViews(){
            showDate = (EditText) findViewById(R.id.showdate);
            pickDate = (Button) findViewById(R.id.pickdate);
            showTime = (EditText)findViewById(R.id.showtime);
            pickTime = (Button)findViewById(R.id.picktime);

            pickDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    if (pickDate.equals((Button) v)) {
                        msg.what = TeamGatherActivity.SHOW_DATAPICK;
                    }
                    TeamGatherActivity.this.dateandtimeHandler.sendMessage(msg);
                }
            });

            pickTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Message msg = new Message();
                    if (pickTime.equals((Button) v)) {
                        msg.what = TeamGatherActivity.SHOW_TIMEPICK;
                    }
                    TeamGatherActivity.this.dateandtimeHandler.sendMessage(msg);
                }
            });
        }

        /**
         * 设置日期
         */
        private void setDateTime(){
            final Calendar c = Calendar.getInstance();

            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            updateDateDisplay();
        }

        /**
         * 更新日期显示
         */
        private void updateDateDisplay(){
            showDate.setText(new StringBuilder().append(mYear).append("-")
                    .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                    .append((mDay < 10) ? "0" + mDay : mDay));
        }

        /**
         * 日期控件的事件
         */
        private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                updateDateDisplay();
            }
        };

        /**
         * 设置时间
         */
        private void setTimeOfDay(){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            updateTimeDisplay();
        }

        /**
         * 更新时间显示
         */
        private void updateTimeDisplay(){
            showTime.setText(new StringBuilder().append(mHour).append(":")
                    .append((mMinute < 10) ? "0" + mMinute : mMinute));
        }

        /**
         * 时间控件事件
         */
        private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;

                updateTimeDisplay();
            }
        };

        @Override
        protected Dialog onCreateDialog(int id) {
            switch (id) {
                case DATE_DIALOG_ID:
                    return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                            mDay);
                case TIME_DIALOG_ID:
                    return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
            }

            return null;
        }

        @Override
        protected void onPrepareDialog(int id, Dialog dialog) {
            switch (id) {
                case DATE_DIALOG_ID:
                    ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                    break;
                case TIME_DIALOG_ID:
                    ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                    break;
            }
        }

        /**
         * 处理日期和时间控件的Handler
         */
        Handler dateandtimeHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TeamGatherActivity.SHOW_DATAPICK:
                        showDialog(DATE_DIALOG_ID);
                        break;
                    case TeamGatherActivity.SHOW_TIMEPICK:
                        showDialog(TIME_DIALOG_ID);
                        break;
                }
            }

        };

    }






















