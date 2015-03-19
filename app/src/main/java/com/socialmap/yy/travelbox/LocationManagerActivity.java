package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.socialmap.yy.travelbox.chat.switcher.Switch;
import com.socialmap.yy.travelbox.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.chat.util.PreferenceUtils;


public class LocationManagerActivity extends Activity implements
        CompoundButton.OnCheckedChangeListener {

    public Switch LocalSwitch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locationmanager);

        LocalSwitch = (Switch)findViewById(R.id.local_switch);
        LocalSwitch.setOnCheckedChangeListener(this);
        LocalSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                this, PreferenceConstants.LM, false));
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.history_menu1, menu);
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.local_switch:
                PreferenceUtils.setPrefBoolean(this,
                        PreferenceConstants.LM, isChecked);
                if (LocalSwitch.isChecked()){

                }
                break;
            default:
                break;
        }
    }











}