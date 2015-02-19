package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
  对应“团队”界面 开发组21号拿到UI设计，根据对应功能进行扩充
 */
public class AllTeamActivity extends Activity implements View.OnTouchListener{
    ImageButton backbutton;
    ListView listView;
    ImageView button;
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_team);
        button = (ImageView)findViewById(R.id.avatar);
        backbutton = (ImageButton)findViewById(R.id.backbutton);
        listView = (ListView)findViewById(R.id.listView);
        backbutton.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(AllTeamActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        String[] AllTeam = new String[]{

                "名称：",
                "发起人：",
                "目的地：",
                "日期：",
                "状态：",
                "日程：",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllTeam);
        listView.setAdapter(adapter);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.setOnTouchListener(this);

        button.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(AllTeamActivity.this,TeamDetailActivity.class);
                startActivity(intent);
                finish();
            }});
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teammain, menu);
        return true;
    }

    //主界面中菜单点击事件响应
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.team_find:
                startActivity(new Intent(this, TeamFindActivity.class));
                break;
            case R.id.team_history:
                startActivity(new Intent(this, TeamHistoryActivity.class));
                break;
            case R.id.team_gather:
                startActivity(new Intent(this, TeamGatherActivity.class));
                break;
            case R.id.team_schedule:
                startActivity(new Intent(this, TeamScheduleHistory.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }











    @Override
    public boolean onTouch(View v,MotionEvent event){
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                int xSpeed = getScrollVelocity();

                if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }
}
