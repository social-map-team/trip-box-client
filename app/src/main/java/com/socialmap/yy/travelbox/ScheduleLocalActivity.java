package com.socialmap.yy.travelbox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.data.DBHelper;
import com.socialmap.yy.travelbox.model.DailyTravelSchedule;
import com.socialmap.yy.travelbox.model.ScheduleEvent;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gxyzw_000 on 2015/3/9.
 */
public class ScheduleLocalActivity extends Activity {

    private List<DayFragment> fragments;
    private LinearLayout indicatorWrapper;
    private int currentDay = 0;

    private static GestureDetector gestureDetector;
    private static List<DailyTravelSchedule> days; // 数据源
    public static DBHelper dbHelper;

    private void getDataSource() {
        List<ScheduleEvent> events = new ArrayList<>();
        dbHelper.open();
        Cursor returnCursor = dbHelper.findList(false, "tItem", new String[]{"id", "t", "title", "info", "level"}, null, null, null, null, "id desc", null);
        while (returnCursor.moveToNext()) {
            String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
            String t = returnCursor.getString(returnCursor.getColumnIndexOrThrow("t"));
            String title = returnCursor.getString(returnCursor.getColumnIndexOrThrow("title"));
            String info = returnCursor.getString(returnCursor.getColumnIndexOrThrow("info"));
            String level = returnCursor.getString(returnCursor.getColumnIndexOrThrow("level"));
            ScheduleEvent event = new ScheduleEvent();
            event.setContent(info);
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                event.setStart(format.parse(t));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            event.setTitle(title);
            event.setLevel(Float.parseFloat(level));
            event.setId(Integer.parseInt(id));
            events.add(event);
        }
        dbHelper.closeConnection();

        Collections.sort(events, new Comparator<ScheduleEvent>() {
            @Override
            public int compare(ScheduleEvent lhs, ScheduleEvent rhs) {
                return (lhs.getStart().after(rhs.getStart())) ? 1 : -1;
            }
        });

        days = new ArrayList<>();
        DailyTravelSchedule day = new DailyTravelSchedule();
        for (ScheduleEvent e : events) {

            int size = day.getEvents().size();
            if (size == 0) {
                day.getEvents().add(e);
            } else {
                ScheduleEvent last = day.getEvents().get(size - 1);
                if (DateUtils.isSameDay(last.getStart(), e.getStart())) {
                    day.getEvents().add(e);
                } else {
                    days.add(day);
                    day = new DailyTravelSchedule();
                    day.getEvents().add(e);
                }
            }
        }
        if (!day.getEvents().isEmpty()) {
            days.add(day);
        }
    }

    private void rightFling() {
        if (currentDay > 0) {
            currentDay = currentDay - 1;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.schedule_list_wrapper, fragments.get(currentDay))
                    .commit();
        }
        updateIndicators();
    }

    private void leftFling() {
        if (currentDay < days.size() - 1) {
            currentDay = currentDay + 1;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.schedule_list_wrapper, fragments.get(currentDay))
                    .commit();
        }
        updateIndicators();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_local);

        // 初始化数据库
        dbHelper = new DBHelper(getBaseContext());

        // 获取数据库内的数据
        getDataSource();

        // 每一天创建一个DayFragment
        gestureDetector =
                new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                    private static final int SWIPE_MIN_DISTANCE = 120;
                    private static final int SWIPE_MAX_OFF_PATH = 250;
                    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        // right to left swipe
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            leftFling();
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            rightFling();
                        }
                        return false;
                    }
                });

        // 添加界面下面的一排小圈圈
        indicatorWrapper = (LinearLayout) findViewById(R.id.schedule_indicator);

        update();

        // 自定义的ActionBar
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        // 加载本地日程ActionBar的布局文件
        View mCustomView = mInflater.inflate(R.layout.activity_schedule_local_actionbar, null);







        // 添加日程按钮
        ImageButton addbutton = (ImageButton) mCustomView.findViewById(R.id.add);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ScheduleLocalActivity.this, ScheduleLocalAddActivity.class);
                startActivity(intent);
            }
        });






        // 删除日程按钮
        ImageButton deletebutton = (ImageButton) mCustomView.findViewById(R.id.delete);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventListAdapter adapter = fragments.get(currentDay).getAdapter();
                if (adapter.getSelectedIndex() == -1) {
                    Toast.makeText(ScheduleLocalActivity.this, "请选择一项来删除", Toast.LENGTH_LONG).show();
                    return;
                }
                // 从数据库中删除
                dbHelper.open();
                DailyTravelSchedule day = days.get(currentDay);
                ScheduleEvent event = day.getEvents().get(adapter.getSelectedIndex());
                int id = event.getId();
                Log.i("yy", "id:" + id);
                dbHelper.execSQL("DELETE FROM tItem WHERE id = " + id);
                dbHelper.closeConnection();

                // 从UI中删除
                day.getEvents().remove(event);
                adapter.setSelectedIndex(-1);
                if (day.getEvents().isEmpty()) {
                    if (currentDay == days.size() - 1) currentDay = 0;
                    days.remove(day);

                }
                update();
            }
        });


        // 编辑日程按钮
        ImageButton editbutton = (ImageButton) mCustomView.findViewById(R.id.edit);
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // 日历按钮
        ImageButton calbutton = (ImageButton) mCustomView.findViewById(R.id.cal);
        calbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleLocalActivity.this, CalendarActivity.class));
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private void showNoEventFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.schedule_list_wrapper, new NoEventFragment())
                .commit();
    }

    private void update() {
        updateFragments();
        updateIndicators();
    }

    private void updateFragments() {
        fragments = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            DayFragment f = new DayFragment();
            f.setDayIndex(i);
            fragments.add(f);
        }

        if (fragments.size() == 0) {
            showNoEventFragment();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.schedule_list_wrapper, fragments.get(currentDay))
                    .commit();
        }
    }

    private void updateIndicators() {
        if (days.size() <= 1) {
            indicatorWrapper.setVisibility(View.GONE);
            return;
        } else {
            indicatorWrapper.setVisibility(View.VISIBLE);
        }

        indicatorWrapper.removeAllViews();
        for (int i = 0; i < days.size(); i++) {
            ImageView circle = (ImageView) getLayoutInflater()
                    .inflate(R.layout.activity_schedule_indicator, indicatorWrapper, false);
            indicatorWrapper.addView(circle);
        }
        ((ImageView) indicatorWrapper.getChildAt(currentDay))
                .setImageResource(R.drawable.schedule_indicator_choosen);
        indicatorWrapper.invalidate();
    }

    public static class DayFragment extends Fragment {
        private int dayIndex;
        private EventListAdapter adapter;

        public void setDayIndex(int dayIndex) {
            this.dayIndex = dayIndex;
        }

        public EventListAdapter getAdapter() {
            return adapter;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_schedule_local_fragment, null);
            ListView list = (ListView) root.findViewById(R.id.list);
            adapter = new EventListAdapter(getActivity(), dayIndex);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position != adapter.getSelectedIndex()) {
                        adapter.selectedIndex = position;
                    } else {
                        adapter.selectedIndex = -1;
                    }
                    adapter.notifyDataSetInvalidated();
                    Log.i("yy", "clicked!");
                }
            });

            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });

            return root;
        }
    }

    public static class NoEventFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_schedule_local_empty, container, false);
        }
    }

    public static class EventListAdapter extends BaseAdapter {

        private int dayIndex;
        private LayoutInflater inflater;
        int selectedIndex = -1;

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        public EventListAdapter(Context context, int dayIndex) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.dayIndex = dayIndex;
        }

        @Override
        public int getCount() {
            return days.get(dayIndex).getEvents().size();
        }

        @Override
        public Object getItem(int position) {
            return days.get(dayIndex).getEvents().get(position);
        }

        @Override
        public long getItemId(int position) {
            return days.get(dayIndex).getEvents().get(position).getId();
        }

        public class ViewHolder {
            public TextView title;
            public TextView content;
            public TextView time;
            public RatingBar level;
            public CheckBox checkbox;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_schedule_local_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.level = (RatingBar) convertView.findViewById(R.id.level);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            DailyTravelSchedule day = days.get(dayIndex);
            ScheduleEvent event = day.getEvents().get(position);
            String title = event.getTitle();
            String content = event.getContent();
            String time = "";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            if (event.getEnd() == null) {
                time = format.format(event.getStart());
            } else {
                time = format.format(event.getStart()) + " ~ " + format.format(event.getEnd());
            }
            float level = event.getLevel();

            holder.title.setText(title);
            holder.content.setText(content);
            holder.time.setText(time);
            holder.level.setRating(level);
            holder.checkbox.setVisibility(View.GONE);
            if (selectedIndex == position) holder.checkbox.setVisibility(View.VISIBLE);

            return convertView;
        }
    }
}