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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.data.DBHelper;
import com.socialmap.yy.travelbox.model.DailyTravelSchedule;
import com.socialmap.yy.travelbox.model.TravelSchedule;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by gxyzw_000 on 2015/3/9.
 */
public class ScheduleLocalActivity extends Activity {



    private ListView list;

    private List<DailyTravelSchedule> days = new ArrayList<>();

    private List<DayFragment> fragments = new LinkedList<>();

    private List<ImageView> indicators = new LinkedList<>();

    private int currentDay = 0;

    public static DBHelper dbHelper;
    private static String itemid="0";
    private static Integer poid=0; //记录listview的当前项位置
    private static ListView mylist;
    private static View itemview;
    private static List<Map<String, Object>> listItem;  // ListView的数据源
    public static SimpleAdapter adapter;
    private static String orderItem="level"; // 排序列

    /**
     * 用于测试
     */
    public void createTable()  {
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
                while((line = reader.readLine()) != null){
                    if (line.isEmpty()){
                        if (hasdata){
                            // insert
                            dbHelper.execSQL(String.format(format, data[0], data[1], data[2], data[3]));
                            hasdata = false;
                            data[0] = data[1] = data[2] = data[3] = null;
                            currentIndex = 0;
                        }
                    } else {
                        hasdata = true;
                        if (line.startsWith("时间：")){
                            data[0] = line.substring(3);
                            currentIndex = 0;
                        } else if(line.startsWith("标题：")){
                            data[1] = line.substring(3);
                            currentIndex = 1;
                        } else if(line.startsWith("简介：")){
                            data[2] = line.substring(3);
                            currentIndex = 2;
                        } else if(line.startsWith("星等：")){
                            data[3] = line.substring(3);
                            currentIndex = 3;
                        } else {
                            data[currentIndex] += line;
                        }
                    }
                }
                if (hasdata){
                    dbHelper.execSQL(String.format(format, data[0], data[1], data[2], data[3]));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        dbHelper.closeConnection();
    }

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
                Intent intent =new Intent();
                intent.setClass(ScheduleLocalActivity.this,AddActivity.class);
                startActivity(intent);

            }
        });



        // 删除日程按钮
        ImageButton deletebutton = (ImageButton) mCustomView.findViewById(R.id.delete);
        deletebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dbHelper.open();
                Log.i("delpoid",poid.toString());
                dbHelper.delete("tItem", "id =?", new String[] { itemid });
                listItem.remove(poid.intValue());
                adapter.notifyDataSetChanged();
                Log.i("delitemid",itemid);
                ShowMsg("删除成功！");
                dbHelper.closeConnection();

            }
        });



        // 编辑日程按钮
        ImageButton editbutton = (ImageButton) mCustomView.findViewById(R.id.edit);
        editbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!itemid.equals("0")){
                    Intent intent =new Intent();
                    intent.setClass(ScheduleLocalActivity.this, EditActivity.class);
                    String id=((TextView)itemview.findViewById(R.id.id)).getText().toString();
                    String title=((TextView)itemview.findViewById(R.id.title)).getText().toString();
                    String t=((TextView)itemview.findViewById(R.id.t)).getText().toString();
                    String info=((TextView)itemview.findViewById(R.id.info)).getText().toString();
                    String level=((TextView)itemview.findViewById(R.id.level)).getText().toString();
                    intent.putExtra("id", id);
                    intent.putExtra("title", title);
                    intent.putExtra("t", t);
                    intent.putExtra("info", info);
                    intent.putExtra("level", level);
                    startActivityForResult(intent, 123);
                }
                else
                    ShowMsg("请选择项目。");

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

        LinearLayout indicatorWrapper = (LinearLayout) findViewById(R.id.schedule_indicator);
        for (int i = 0; i < days.size(); i++) {
            ImageView circle = (ImageView) getLayoutInflater().inflate(R.layout.activity_schedule_indicator, indicatorWrapper, false);
            indicators.add(circle);
            indicatorWrapper.addView(circle);
        }
        Log.i("yy", indicatorWrapper.getChildCount() + "");
        indicatorWrapper.invalidate();

        indicators.get(0).setImageResource(R.drawable.schedule_indicator_choosen);
    }

    private void updateIndicators() {
        for (ImageView v : indicators) {
            v.setImageResource(R.drawable.schedule_indicator);
        }
        indicators.get(currentDay).setImageResource(R.drawable.schedule_indicator_choosen);
    }























    private  List<Map<String,Object>> listDataOrder( List<Map<String,Object>> list){
        if (!list.isEmpty()) {
            Collections.sort(list, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> object1, Map<String, Object> object2) {
                    //根据文本排序
                    return ((String) object1.get(orderItem)).compareTo((String) object2.get(orderItem));
                }
            });
        }
        return list;
    }



    //显示提示信息
    public  void ShowMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

















    public static class  DayFragment extends Fragment {
        private DailyTravelSchedule day;
        private static LayoutInflater inflater;
        private GestureDetector gestureDetector;




        public  void ShowMsg(String msg){
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }


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


        private List<Map<String,Object>> getlistdata(){
            List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
            dbHelper.open();
            Cursor returnCursor = dbHelper.findList(false, "tItem", new String[] { "id", "t", "title","info","level" }, null, null, null, null, "id desc", null);
            while (returnCursor.moveToNext()) {
                String id = returnCursor.getString(returnCursor.getColumnIndexOrThrow("id"));
                String t= returnCursor.getString(returnCursor.getColumnIndexOrThrow("t"));
                String title = returnCursor.getString(returnCursor.getColumnIndexOrThrow("title"));
                String info = returnCursor.getString(returnCursor.getColumnIndexOrThrow("info"));
                String level = returnCursor.getString(returnCursor.getColumnIndexOrThrow("level"));
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("id", id);
                if (level.equals("1"))
                    map.put("img",R.drawable.img1);
                else
                    map.put("img",R.drawable.img2);
                map.put("t", t);
                map.put("title",title );
                map.put("info", info);
                map.put("level", level);
                list.add(map);
            }
            dbHelper.closeConnection();
            return list;
        }


        private AdapterView.OnItemClickListener ItemClick=new  AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //恢复上一选定项颜色
                if (itemview!=null) itemview.setBackgroundResource(R.color.white);
                //取消展开
                if (itemview!=null) ((TextView)itemview.findViewById(R.id.info)).setVisibility(View.GONE);
                //设置当前选定项颜色
                arg1.setBackgroundResource(R.color.holo_blue);
                //保存选定项id
                itemid=((TextView)arg1.findViewById(R.id.id)).getText().toString();
                Log.i("getitemid",itemid);
                poid=arg2;
                Log.i("getpoid",poid.toString());
                //保存当前项目view
                itemview=arg1;
                String title=((TextView)arg1.findViewById(R.id.title)).getText().toString();
                String t=((TextView)arg1.findViewById(R.id.t)).getText().toString();
                String info=((TextView)arg1.findViewById(R.id.info)).getText().toString();
                ((TextView)arg1.findViewById(R.id.info)).setVisibility(View.VISIBLE);
                ShowMsg(new StringBuilder().append(title).append("\n").append(t).append("\n").append(info).toString());
            }
        };



        private  List<Map<String,Object>> listDataOrder( List<Map<String,Object>> list){
            if (!list.isEmpty()) {
                Collections.sort(list, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> object1, Map<String, Object> object2) {
                        //根据文本排序
                        return ((String) object1.get(orderItem)).compareTo((String) object2.get(orderItem));
                    }
                });
            }
            return list;
        }










        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            View itemview = inflater.inflate(R.layout.activity_schedule_fragment, null);
            ListView mylist = (ListView) itemview.findViewById(R.id.tb_schedule_list);
            listItem=getlistdata();
            adapter = new SimpleAdapter(getActivity(),listItem,R.layout.mylist, new String[]{"id","img","t","title","info","level"}, new int[]{R.id.id,R.id.img,R.id.t,R.id.title,R.id.info,R.id.level});
            mylist.setAdapter(adapter);
            mylist.setOnItemClickListener(ItemClick);

            if (orderItem.equals("level")) orderItem="t";else orderItem="level";
            listItem=listDataOrder(listItem);
            adapter.notifyDataSetChanged();


            mylist.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
            assert day != null;




            return itemview;
        }


    }
}