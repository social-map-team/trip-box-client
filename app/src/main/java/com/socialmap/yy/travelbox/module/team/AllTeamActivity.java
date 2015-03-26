package com.socialmap.yy.travelbox.module.team;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.module.schedule.ScheduleActivity;
import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;

/**
 * 对应“团队”界面 开发组21号拿到UI设计，根据对应功能进行扩充
 */
public class AllTeamActivity extends Activity implements View.OnTouchListener {
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    ImageButton backbutton;
    ListView listView;
    ImageView button;
    String content;
    String content1;
    String content2;
    String content3;
    String content4;
    String content5;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private EditText inputString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_team);
        button = (ImageView) findViewById(R.id.avatar);

        SharedPreferences sp = getSharedPreferences("team", Context.MODE_WORLD_READABLE);

        content = sp.getString("teamname", "");

        content1 = sp.getString("teamman", "");

        content2 = sp.getString("teamlocal", "");


        content3 = sp.getString("teamdata", "");

        content4 = sp.getString("teamother", "");

        content5 = sp.getString("teamstatus", "");


        listView = (ListView) findViewById(R.id.listView);

        String[] AllTeam = new String[]{

                "名称：" + content,
                "发起人：" + content1,
                "目的地：" + content2,
                "日期：" + content3,
                "备注" + content4,
                "状态：" + content5,

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, AllTeam);
        listView.setAdapter(adapter);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.setOnTouchListener(this);

        button.setOnClickListener(new ImageButton.OnClickListener() {
            //TODO 跳转
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AllTeamActivity.this, TeamDetailActivity.class);
                startActivity(intent);

            }
        });
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
                LayoutInflater inflater = (LayoutInflater) AllTeamActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(
                        R.layout.input_add, null);
                inputString = (EditText) layout
                        .findViewById(R.id.input_add_string);

                new AlertDialog.Builder(AllTeamActivity.this)
                        .setTitle("请输入想要添加的团队")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(layout)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        String str = inputString.getText().toString();
                                        //加入团队
                                        getInstance()
                                                .request("/profile/team/{id}", "post",
                                                        "id", str
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

                                        //获取团队
                                        getInstance()
                                                .request("/profile/team/{id}", "get",
                                                        "id", str
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

                                        content = "";
                                        content1 = "";
                                        content2 = "";
                                        content3 = "";
                                        content4 = "";
                                        content5 = "";


                                        //todo get方法 怎么从服务器读取资源 还有日程的读取


                                        SharedPreferences.Editor editor = getSharedPreferences("team", Context.MODE_WORLD_WRITEABLE).edit();
                                        //将EditText中的文本内容添加到编辑器
                                        editor.putString("teamname", content);
                                        //提交编辑器内容
                                        editor.putString("teamman", content1);
                                        editor.putString("teamlocal", content2);
                                        editor.putString("teamdata", content3);
                                        editor.putString("teamother", content4);
                                        editor.putString("teamstatus", content5);

                                        editor.commit();


                                    }


                                })
                        .setNegativeButton("取消", null)
                        .show();


            case R.id.team_history:
                startActivity(new Intent(this, TeamHistoryActivity.class));
                break;
            case R.id.team_gather:
                startActivity(new Intent(this, TeamGatherActivity.class));
                break;
            case R.id.team_schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.team_create:
                startActivity(new Intent(this, TeamCreateActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
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
