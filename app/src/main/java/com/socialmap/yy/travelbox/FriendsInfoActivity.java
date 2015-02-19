package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;


public class FriendsInfoActivity extends Activity {
    ImageButton delete,Talk,LocationShare,history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        delete = (ImageButton)findViewById(R.id.delete);
        Talk   = (ImageButton)findViewById(R.id.Talk);
        LocationShare = (ImageButton)findViewById(R.id.LocationShare);
        history = (ImageButton)findViewById(R.id.history);

    }
}
