package com.socialmap.yy.travelbox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.model.DailyTravelSchedule;
import com.socialmap.yy.travelbox.model.TravelSchedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ScheduleActivity extends Activity {
    ImageButton backbutton;

    private ListView list;

    private List<DailyTravelSchedule> days = new ArrayList<>();

    private List<DayFragment> fragments = new LinkedList<>();

    private List<ImageView> indicators = new LinkedList<>();

    private int currentDay = 0;


    private void initDays() {
        String[] days_text = getResources().getStringArray(R.array.seven_days);
        for (int i = 0; i < 7; i++) {
            DailyTravelSchedule d = new DailyTravelSchedule();
            TravelSchedule t = new TravelSchedule();
            t.setContent(days_text[i]);
            t.setStart(new Date());
            t.setEnd(new Date());
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            d.getSchedules().add(t);
            days.add(d);

        }
    }

    private GestureDetector gestureDetector;

    private void rightFling() {
        if (currentDay > 0) {
            currentDay = currentDay - 1;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right)
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

        //custom actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.activity_schedule_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("World in 7 Days");

        backbutton = (ImageButton)findViewById(R.id.backbutton);
        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });
        /*backbutton.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转 具体什么问题不清楚，第125行报错，应该是返回按钮和其他东西冲突
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ScheduleActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
*/
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        //custom actionbar end

        // initialize days for test
        initDays();

        // create a fragment for each day

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_MIN_DISTANCE = 120;
            private static final int SWIPE_MAX_OFF_PATH = 250;
            private static final int SWIPE_THRESHOLD_VELOCITY = 200;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("yy", "left");
                    leftFling();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Log.i("yy", "right");
                    rightFling();
                }
                return false;
            }
        });

        for (DailyTravelSchedule d : days) {
            DayFragment f = new DayFragment(this);
            f.setDay(d);
            f.setGestureDetector(gestureDetector);
            fragments.add(f);
        }
        getFragmentManager().beginTransaction().replace(R.id.schedule_list_wrapper, fragments.get(0)).commit();

        LinearLayout indicatorWrapper = (LinearLayout)findViewById(R.id.schedule_indicator);
        for(int i=0;i<days.size();i++){
            ImageView circle = (ImageView)getLayoutInflater().inflate(R.layout.activity_schedule_indicator,indicatorWrapper,false);
            indicators.add(circle);
            indicatorWrapper.addView(circle);
        }
        Log.i("yy",indicatorWrapper.getChildCount()+"");
        indicatorWrapper.invalidate();

        indicators.get(0).setImageResource(R.drawable.schedule_indicator_choosen);
    }

    private void updateIndicators(){
        for(ImageView v:indicators){
            v.setImageResource(R.drawable.schedule_indicator);
        }
        indicators.get(currentDay).setImageResource(R.drawable.schedule_indicator_choosen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_my_schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.action_nearby:
                startActivity(new Intent(this, NearbyActivity.class));
                break;
            case R.id.action_account:
                startActivity(new Intent(this, ProfileActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyActionProvider extends ActionProvider {

        private Context mContext;

        public MyActionProvider(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public View onCreateActionView() {
            return null;
        }

        @Override
        public void onPrepareSubMenu(SubMenu subMenu) {
            subMenu.clear();
            subMenu.add("aaaa");
            subMenu.add("bbbb");
            subMenu.add("cccc");
        }

        @Override
        public boolean hasSubMenu() {
            return true;
        }
    }

    public static class DayFragment extends Fragment {
        private DailyTravelSchedule day;
        private static LayoutInflater inflater;
        private GestureDetector gestureDetector;

        public DayFragment(Context context) {
            super();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setDay(DailyTravelSchedule d) {
            day = d;
        }

        public void setGestureDetector(GestureDetector gestureDetector) {
            this.gestureDetector = gestureDetector;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_schedule_fragment, null);
            ListView list = (ListView) root.findViewById(R.id.tb_schedule_list);

            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
            assert day != null;
            list.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return day.getSchedules().size();
                }

                @Override
                public Object getItem(int i) {
                    return day.getSchedules().get(i);
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = inflater.inflate(R.layout.activity_schedule_list_item, null);
                        TextView content = (TextView) convertView.findViewById(R.id.content);
                        TextView start = (TextView) convertView.findViewById(R.id.start);
                        TextView end = (TextView) convertView.findViewById(R.id.end);
                        TravelSchedule t = (TravelSchedule) getItem(position);
                        content.setText(t.getContent());
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                        start.setText(df.format(t.getStart()));
                        end.setText(df.format(t.getEnd()));
                    }
                    return convertView;
                }
            });
            return root;
        }
    }
}
