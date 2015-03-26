package com.socialmap.yy.travelbox.module.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.listview.PullToZoomListView;
import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;


public class FriendsInfoActivity extends Activity {


    PullToZoomListView listView;
    private VelocityTracker mVelocityTracker;
    private String[] adapterData;


    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private String userdata;
    private Button button;
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        listView = (PullToZoomListView) findViewById(R.id.listview);

        Intent intent = this.getIntent();
        userdata = intent.getStringExtra("user");//用户的id（username）


        getInstance()
                .request(" /api/user/{id} ", "GET",
                        "id", userdata
                )
                .execute(new TbsClient.Callback() {
                    @Override
                    public void onFinished(TbsClient.ServerResponse response) {
                        try {
                            String content = new String(response.getContent(), "UTF-8");
                            Log.i("yy", response.getStatusCode() + "\n" + content);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
//todo get获取的用户信息怎么读取

        str1 = "";
        str2 = "";
        str3 = "";
        str4 = "";
        str5 = "";
        str6 = "";


        adapterData = new String[]{
                "用户名:" + str1,
                "身份证号:" + str2,
                "真实姓名:" + str3,
                "生日：" + str4,
                "联系电话:" + str5,
                "联系邮箱:" + str6
        };

        listView.setAdapter(new ArrayAdapter<String>(FriendsInfoActivity.this,
                android.R.layout.simple_list_item_1, adapterData));
        listView.getHeaderView().setImageResource(R.drawable.splash01);
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        button = (Button) findViewById(R.id.chat);
        button.setOnClickListener(new ImageButton.OnClickListener() {
            //TODO 跳转
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FriendsInfoActivity.this, FriendChatActivity.class);
                startActivity(intent);

            }
        });


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friendinfo_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        return super.onPrepareOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.more:
                // create intent to perform web search for this planet
                startActivity(new Intent(this, FriendsSettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


}








