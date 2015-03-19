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

    public void createTable()  {
        dbHelper = new DBHelper(this.getBaseContext());
        dbHelper.open();

        //String deleteSql = "drop table if exists tItem ";
        //dbHelper.execSQL(deleteSql);

        // id：自动递增
        // title: 标题 概要
        // info：详细内容
        // t: 日期时间
        if (!dbHelper.isTableExist("tItem"))   //判断表是否存在
        {
            //创建表
            String sql = "CREATE TABLE tItem (id integer primary key autoincrement, title text, info text,t datatime,level integer)";
            dbHelper.execSQL(sql);
            //初始化两条数据
            sql="insert into tItem(title,info,t,level) values('龙虎山','走透明栈道看悬棺崖墓' ,'3-10-2015 8:00',2)";
            dbHelper.execSQL(sql);
            sql="insert into tItem(title,info,t,level) values('天师符','天师府全称“嗣汉天师府”，亦称“大真人府” 府第坐落在江西贵溪上清古镇，南朝琵琶峰，面临上清河（古称沂溪），北倚西华山，东距大上清官二华里，西离龙虎山主峰十五里许。整个府第由府门、大堂、后堂、私第、殿宇、书屋、花园等部分构成。规模宏大，雄伟壮观，建筑华丽，工艺精致，是一处王府式样的建筑，也是中国现存封建社会“大府第”之一。院内豫樟成林，古木参天，浓荫散绿，环境清幽，昔有“仙都”，“南国第一家”之称。' ,'3-10-2015 11:00',1)";
            dbHelper.execSQL(sql);
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
        createTable();  //链接数据库并检查表是否存在，不存在则创建表
        //custom actionbar
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.activity_schedule_local_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("World in 7 Days");

        ImageButton addbutton = (ImageButton) mCustomView.findViewById(R.id.add);
        ImageButton deletebutton = (ImageButton) mCustomView.findViewById(R.id.delete);
        ImageButton editbutton = (ImageButton) mCustomView.findViewById(R.id.edit);
        ImageButton calbutton = (ImageButton) mCustomView.findViewById(R.id.cal);



        calbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleLocalActivity.this, CalendarActivity.class));

            }
        });



        addbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(ScheduleLocalActivity.this,AddActivity.class);
                startActivity(intent);

            }
        });

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





        calbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });







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

















    public static class DayFragment extends Fragment {
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