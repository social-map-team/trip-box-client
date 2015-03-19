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


public class EditActivity extends Activity {
    private TextView mDateDisplay;
    private Button btnSetDate;
    private Button btnSetTime;
    private Button btnSave;
    private Button btnCancel;
    private EditText txtTitle;
    private EditText txtInfo;
    private CheckBox cbLev;
    private String itemid;


    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;



    public void ShowMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private DatePickerDialog.OnDateSetListener mSetDateListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int myear, int monthOfYear,int dayOfMonth) {

            year = myear;
            month = monthOfYear;
            day = dayOfMonth;

            updateDisplay();
        }
    };

    private TimePickerDialog.OnTimeSetListener mSetTimeListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int mhour, int mminute) {

            hour = mhour;
            minute = mminute;
            updateDisplay();
        }
    };

    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);


        mDateDisplay = (TextView) findViewById(R.id.tvDate);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtInfo = (EditText) findViewById(R.id.txtInfo);
        cbLev=(CheckBox)findViewById(R.id.cbLev);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mDateDisplay.setText(extras.getString("id"));
            txtTitle.setText(extras.getString("title"));
            txtInfo.setText(extras.getString("info"));
            itemid=extras.getString("id");
            if (extras.getString("level").equals("1"))
                cbLev.setChecked(true);
            else
                cbLev.setChecked(false);
        }


        btnSetDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);
            }
        });


        btnSetTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ScheduleLocalActivity.dbHelper.open();
                ContentValues values = new ContentValues();
                values.put("title", txtTitle.getText().toString());
                values.put("info",  txtInfo.getText().toString());
                values.put("t",  mDateDisplay.getText().toString());
                if (cbLev.isChecked())
                    values.put("level",1);
                else
                    values.put("level",2);


                ScheduleLocalActivity.dbHelper.update("tItem", values, "id = ?", new String[] { itemid});
                ScheduleLocalActivity.dbHelper.closeConnection();
                ShowMsg("修改成功");

                Intent intent =new Intent();
                intent.setClass(EditActivity.this,ScheduleLocalActivity.class);
                startActivity(intent);
                EditActivity.this.finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.setClass(EditActivity.this,MainActivity.class);
                startActivity(intent);
                EditActivity.this.finish();
            }
        });


        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);


        updateDisplay();
    }

    /**
     *
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
     *
     */
    private void updateDisplay() {
        mDateDisplay.setText(new StringBuilder()// Month is 0 based so add 1
                .append(month + 1).append("-").append(day).append("-").append(year).append(" ").append(hour).append(":").append(minute));
    }



}

