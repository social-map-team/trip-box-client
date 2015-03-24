package com.socialmap.yy.travelbox.team;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;

/**
 * Created by gxyzw_000 on 2015/3/20.
 */
public class TeamCreateActivity extends Activity {

    private static String TAG = "HelloPreference";

    private EditText team_name;

    private EditText team_man;

    private EditText  team_local;

    private EditText  team_data;

    private EditText  team_other;

    private TextView team_status;
    private Button submit ;


    Context mContext = null;

    private String contentsex = " ";
    private int genderIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

     setContentView(R.layout.activity_team_create);





        team_name = (EditText) findViewById(R.id.team_name);
        team_man = (EditText) findViewById(R.id.team_man);
        team_local = (EditText) findViewById(R.id.team_local);
        team_data = (EditText)findViewById(R.id.team_data);
        team_other = (EditText) findViewById(R.id.team_other);
        team_status = (TextView) findViewById(R.id.team_status);
        submit = (Button) findViewById(R.id.submit);



        SharedPreferences sp = getSharedPreferences("team", Context.MODE_WORLD_READABLE);

        String content = sp.getString("teamname","");
        team_name.setText(content);

        String content1 = sp.getString("teamman","");
        team_man.setText(content1);

        String content2 = sp.getString("teamlocal","");
        team_local.setText(content2);


        String content3 = sp.getString("teamdata","");
        team_data.setText(content3);

        String content4 = sp.getString("teamother","");
        team_other.setText(content4);

        String content5 = sp.getString("teamstatus","");
        team_status.setText(content5);




        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                submit();
            }
        });





        team_status.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeamCreateActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择性别");
                final String[] sex = {"不可加入", "可以加入", "未公开"};

                builder.setSingleChoiceItems(sex, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TeamCreateActivity.this, "状态为：" + sex[which], Toast.LENGTH_SHORT).show();
                        genderIndex  = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        team_status.setText(sex[genderIndex]);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });


}









    public void onStop() {
        super.onStop();
        //获得编辑器
        SharedPreferences.Editor editor = getSharedPreferences("team", Context.MODE_WORLD_WRITEABLE).edit();
        //将EditText中的文本内容添加到编辑器
        editor.putString("teamname", team_name.getText().toString());
        //提交编辑器内容
        editor.putString("teamman", team_man.getText().toString());
        editor.putString("teamlocal", team_local.getText().toString());
        editor.putString("teamdata", team_data.getText().toString());
        editor.putString("teamother", team_other.getText().toString());
        editor.putString("teamstatus",  team_status.getText().toString());



        editor.commit();
    }






    private void submit() {



    }





}
