package com.socialmap.yy.travelbox.module.team;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.socialmap.yy.travelbox.R;

/**
 * .  此为团队成员信息界面,图标从左往右为添加好友、聊天、位置分享、举报
 */
public class TeamMateInfoActivity  extends Activity {
    ImageButton add,Talk,LocationShare,jubao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_mate);
        add = (ImageButton)findViewById(R.id.add);
        Talk   = (ImageButton)findViewById(R.id.Talk);
        LocationShare = (ImageButton)findViewById(R.id.LocationShare);
        jubao = (ImageButton)findViewById(R.id.jubao);

    }
}

