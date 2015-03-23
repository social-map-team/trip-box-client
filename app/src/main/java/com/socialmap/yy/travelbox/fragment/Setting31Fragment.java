package com.socialmap.yy.travelbox.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.socialmap.yy.travelbox.R;

/**
 * Created by gxyzw_000 on 2015/3/4.
 */
public class Setting31Fragment extends PreferenceFragment
        implements   Preference.OnPreferenceClickListener {
    private static CheckBoxPreference verifyPreference;
    private static CheckBoxPreference allPreference;
    private static CheckBoxPreference friendPreference;
    private static CheckBoxPreference teammatePreference;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting31_prefs);

        CheckBoxPreference verifyPreference = (CheckBoxPreference) findPreference("verify");
        CheckBoxPreference allPreference = (CheckBoxPreference) findPreference("all");
        CheckBoxPreference  friendPreference = (CheckBoxPreference) findPreference("friend");
        CheckBoxPreference teammatePreference = (CheckBoxPreference) findPreference("teammate");

        verifyPreference.setOnPreferenceClickListener(this);
        //pushPreference.setOnPreferenceChangeListener(this);
        allPreference.setOnPreferenceClickListener(this);
        //messagePreference.setOnPreferenceChangeListener(this);
        friendPreference.setOnPreferenceClickListener(this);
        //soundPreference.setOnPreferenceChangeListener(this);
        teammatePreference.setOnPreferenceClickListener(this);
        //vibratePreference.setOnPreferenceChangeListener(this);

        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean verifycheck = shp.getBoolean("verify", false);
        boolean allcheck = shp.getBoolean("all", false);
        boolean friendcheck = shp.getBoolean("friend", false);
        boolean teammatecheck = shp.getBoolean("teammate", false);

    }


    public boolean onPreferenceClick(Preference preference) {
        if (preference == verifyPreference) {
            if (verifyPreference.isChecked()) {
//todo 具体操作
            }
        } else if (preference == allPreference) {

        } else if (preference == friendPreference) {

        } else if (preference == teammatePreference) {

        }
        return false;
    }

}
/*
        public boolean onPreferenceChange(Preference preference, Object objValue) {
            if (preference == pushPreference) {
                Log.i(TAG, "Wifi CB, and isCheckd = " + String.valueOf(objValue));
            } else if (preference == messagePreference) {
                Log.i(TAG, "internet CB, and isCheckd = " + String.valueOf(objValue));
                return false;  //不保存该新值
            }
            return true;  //保存更新后的值

        }
*/



