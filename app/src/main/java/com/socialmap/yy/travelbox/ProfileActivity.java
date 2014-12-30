package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by yy on 7/24/14.
 */
public class ProfileActivity extends Activity implements View.OnTouchListener {
    ImageButton friends,LocationManager,Settings,history;
    ImageView button;

    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ListView list = (ListView) findViewById(R.id.list);
        button = (ImageView)findViewById(R.id.avatar_wrapper);
        friends = (ImageButton)findViewById(R.id.friends);
        LocationManager = (ImageButton)findViewById(R.id.LocationManager);
        Settings = (ImageButton)findViewById(R.id.Settings);
        history = (ImageButton)findViewById(R.id.history);
        button.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        friends.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,FriendsActivity.class);
                startActivity(intent);
                finish();

            }
        });
        history.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,historyActivity.class);
                startActivity(intent);
                finish();

            }
        });
        LocationManager.setOnClickListener(new ImageButton.OnClickListener(){
            //TODO 跳转
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,LocationManagerActivity.class);
                startActivity(intent);
                finish();

            }
        });
        Settings.setOnClickListener(new ImageButton.OnClickListener(){

            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(ProfileActivity.this,SettingTabActivity.class);
                startActivity(intent);
                finish();
            }


        });
      /*  String[] profile = new String[]{
                "用户名：bitdancer",
                "身份证号：XXXXXXXXXXXXXXXXXX",
                "真实姓名：杨阳",
                "生日：1992年3月19日",
                "联系电话: +86XXXXXXXXXXX",
                "联系邮箱: oplogobit@gmail.com"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, profile);
        list.setAdapter(adapter);
        */
        RelativeLayout ll = (RelativeLayout) findViewById(R.id.ll);
        ll.setOnTouchListener(this);
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
