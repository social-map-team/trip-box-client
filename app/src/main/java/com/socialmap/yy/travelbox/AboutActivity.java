package com.socialmap.yy.travelbox;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.socialmap.yy.travelbox.module.chat.swipeback.SwipeBackActivity;
import com.socialmap.yy.travelbox.module.chat.view.ChangeLog;


public class AboutActivity extends SwipeBackActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView tv = (TextView) findViewById(R.id.website);
		Linkify.addLinks(tv, Linkify.ALL);


	}








	public void showChangeLog(View view) {
		new ChangeLog(this).getFullLogDialog().show();
	}
}
