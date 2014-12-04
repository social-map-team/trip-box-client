package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

/**
 * Created by LZY on 2014/12/4.  图标从左往右为添加好友、聊天、位置分享、举报
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

