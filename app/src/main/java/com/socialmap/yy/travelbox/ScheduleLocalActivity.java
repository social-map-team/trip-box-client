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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.socialmap.yy.travelbox.data.DBHelper;
import com.socialmap.yy.travelbox.model.DailyTravelSchedule;
import com.socialmap.yy.travelbox.model.ScheduleEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gxyzw_000 on 2015/3/9.
 */
public class ScheduleLocalActivity extends Activity {


    private List<DayFragment> fragments = new LinkedList<>();
    private List<ImageView> indicators = new LinkedList<>();
    private int currentDay = 0;

    private static GestureDetector gestureDetector;
    private static List<DailyTravelSchedule> days = new ArrayList<>(); // 数据源
    public static DBHelper dbHelper;

    /**
     * 用于测试
     */
    public void createTable() {
        dbHelper = new DBHelper(this.getBaseContext());
        dbHelper.open();

        String deleteSql = "drop table if exists tItem ";
        dbHelper.execSQL(deleteSql);

        // id：自动递增
        // title: 标题 概要
        // info：详细内容
        // t: 日期时间
        if (true || !dbHelper.isTableExist("tItem"))   //判断表是否存在
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
    }

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
            events.add(event);
        }
        dbHelper.closeConnection();

        Collections.sort(events, new Comparator<ScheduleEvent>() {
            @Override
            public int compare(ScheduleEvent lhs, ScheduleEvent rhs) {
                return (lhs.getStart().after(rhs.getStart())) ? 1 : -1;
            }
        });
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
        days.add(day);
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
        setContentView(R.layout.activity_schedule);

        // 首先，检查数据库，如果表不存在就新建表
        createTable();

        // 获取数据库内的数据
        getDataSource();

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
                intent.setClass(ScheduleLocalActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        // 删除日程按钮
        ImageButton deletebutton = (ImageButton) mCustomView.findViewById(R.id.delete);
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.open();
                // 删除选中的事件项
                // 从数据库中删除
                // dbHelper.delete("tItem", "id =?", new String[]{itemid});
                // 从数据源中删除
                // adapter.notifyDataSetChanged();
                dbHelper.closeConnection();
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
        // 完成自定义ActionBar


        // create a fragment for each day
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

        for (int i = 0; i< days.size(); i++) {
            DayFragment f = new DayFragment();
            f.setDayIndex(i);
            fragments.add(f);
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.schedule_list_wrapper, fragments.get(0))
                .commit();

        // 添加界面下面的一排小圈圈
        LinearLayout indicatorWrapper = (LinearLayout) findViewById(R.id.schedule_indicator);
        for (int i = 0; i < days.size(); i++) {
            ImageView circle = (ImageView) getLayoutInflater()
                    .inflate(R.layout.activity_schedule_indicator, indicatorWrapper, false);
            indicators.add(circle);
            indicatorWrapper.addView(circle);
        }
        indicatorWrapper.invalidate();
        indicators.get(0).setImageResource(R.drawable.schedule_indicator_choosen);
    }

    private void updateIndicators() {
        for (ImageView v : indicators) {
            v.setImageResource(R.drawable.schedule_indicator);
        }
        indicators.get(currentDay).setImageResource(R.drawable.schedule_indicator_choosen);
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

        /*private AdapterView.OnItemClickListener ItemClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //恢复上一选定项颜色
                if (itemview != null) itemview.setBackgroundResource(R.color.white);
                //取消展开
                if (itemview != null)
                    ((TextView) itemview.findViewById(R.id.info)).setVisibility(View.GONE);
                //设置当前选定项颜色
                arg1.setBackgroundResource(R.color.holo_blue);
                //保存选定项id
                itemid = ((TextView) arg1.findViewById(R.id.id)).getText().toString();
                Log.i("getitemid", itemid);
                poid = arg2;
                Log.i("getpoid", poid.toString());
                //保存当前项目view
                itemview = arg1;
                String title = ((TextView) arg1.findViewById(R.id.title)).getText().toString();
                String t = ((TextView) arg1.findViewById(R.id.t)).getText().toString();
                String info = ((TextView) arg1.findViewById(R.id.info)).getText().toString();
                ((TextView) arg1.findViewById(R.id.info)).setVisibility(View.VISIBLE);
                ShowMsg(new StringBuilder().append(title).append("\n").append(t).append("\n").append(info).toString());
            }
        };*/

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_schedule_fragment, null);
            ListView list = (ListView) root.findViewById(R.id.list);
            adapter = new EventListAdapter(getActivity(), dayIndex);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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

    public static class EventListAdapter extends BaseAdapter {

        private int dayIndex;
        private LayoutInflater inflater;

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
            return 0; // 没有数字ID，所以都返回0
        }

        private class ViewHolder {
            public TextView title;
            public TextView content;
            public TextView time;
            public RatingBar level;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.schedule_event_item, parent, false);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.level = (RatingBar) convertView.findViewById(R.id.level);
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

            return convertView;
        }
    }
}