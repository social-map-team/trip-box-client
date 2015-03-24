package com.socialmap.yy.travelbox.module.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.socialmap.yy.travelbox.R;
import com.squareup.timessquare.CalendarPickerView;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by gxyzw_000 on 2015/3/13.
 */
public class ScheduleLocalCalendarActivity extends Activity {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


    private Date firstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = "01";
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String year = String.format("%04d", cal.get(Calendar.YEAR));
        try {
            return format.parse(String.format("%s-%s-%s", year, month, day));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String month = String.format("%02d", cal.get(Calendar.MONTH) + 1);
        String year = String.format("%04d", cal.get(Calendar.YEAR));
        try {
            Date d = format.parse(String.format("%s-%s-%s", year, month, day));
            cal.setTime(d);
            cal.add(Calendar.DATE, 1);
            return cal.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_local_calendar);

        getActionBar().setTitle("aaa");

        final List<Date> dates = new ArrayList<>();
        Date selectedDate;
        Date minDate;
        Date maxDate;
        try {
            Intent intent = getIntent();
            String datesStr = intent.getStringExtra("dates");
            String[] parts = datesStr.split(" ");

            for (int i = 0; i < parts.length; i++) {
                dates.add(format.parse(parts[i]));
            }

            int currentDay = intent.getIntExtra("currentDay", 0);
            selectedDate = dates.get(currentDay);
            minDate = dates.get(0);
            maxDate = dates.get(dates.size() - 1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.init(firstDayOfMonth(minDate), lastDayOfMonth(maxDate))
                .inMode(CalendarPickerView.SelectionMode.SINGLE)
                .withHighlightedDates(dates)
                .withSelectedDate(selectedDate);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                for (int i=0;i<dates.size();i++){
                    if (DateUtils.isSameDay(date, dates.get(i))){
                        Intent intent = new Intent(ScheduleLocalCalendarActivity.this, ScheduleLocalActivity.class);
                        intent.putExtra("currentDay", i);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {
                Log.i("yy", (new SimpleDateFormat("MM-dd")).format(date) + "取消选中");
            }
        });


    }


}
