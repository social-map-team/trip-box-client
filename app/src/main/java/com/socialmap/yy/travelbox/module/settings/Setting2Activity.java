package com.socialmap.yy.travelbox.module.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.module.chat.switcher.Switch;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceUtils;




    public class Setting2Activity extends FragmentActivity {

    private static Switch mNotifyRunBackgroundSwitch; //开启信息推送
    private static Switch mNewMsgSoundSwitch;//声音开启
    private static Switch mNewMsgVibratorSwitch;//震动开启
    private static Switch mVisiableNewMsgSwitch;    //消息免打扰


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Holo_Light_NoActionBar);

        setContentView(R.layout.activity_setting2);// 设置背景
        FragmentTransaction mFragementTransaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment mFrag = new PrefsFragement();
        mFragementTransaction.replace(R.id.fragment2, mFrag);




    }


    public static class PrefsFragement extends Fragment implements
            CompoundButton.OnCheckedChangeListener {




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.setting2fragment, container,
                    false);
        }



        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);



            mNotifyRunBackgroundSwitch = (Switch) view .findViewById(R.id.notify_run_background_switch);
            mNotifyRunBackgroundSwitch.setOnCheckedChangeListener(this);
            mNewMsgSoundSwitch = (Switch) view .findViewById(R.id.new_msg_sound_switch);
            mNewMsgSoundSwitch.setOnCheckedChangeListener(this);
            mNewMsgVibratorSwitch = (Switch) view.findViewById(R.id.new_msg_vibrator_switch);
            mNewMsgVibratorSwitch.setOnCheckedChangeListener(this);
            mVisiableNewMsgSwitch = (Switch) view.findViewById(R.id.visiable_new_msg_switch);
            mVisiableNewMsgSwitch.setOnCheckedChangeListener(this);

        }

        @Override
        public void onResume() {
            super.onResume();
            readData();
        }

        public void readData() {

            mNotifyRunBackgroundSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                    getActivity(), PreferenceConstants.FOREGROUND, true));
            mNewMsgSoundSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                    getActivity(), PreferenceConstants.SCLIENTNOTIFY, false));
            mNewMsgVibratorSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                    getActivity(), PreferenceConstants.VIBRATIONNOTIFY, true));
            mVisiableNewMsgSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                    getActivity(), PreferenceConstants.TICKER, true));
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.notify_run_background_switch:
                    PreferenceUtils.setPrefBoolean(getActivity(),
                            PreferenceConstants.FOREGROUND, isChecked);
                    break;
                case R.id.new_msg_sound_switch:
                    PreferenceUtils.setPrefBoolean(getActivity(),
                            PreferenceConstants.SCLIENTNOTIFY, isChecked);
                    break;
                case R.id.new_msg_vibrator_switch:
                    PreferenceUtils.setPrefBoolean(getActivity(),
                            PreferenceConstants.VIBRATIONNOTIFY, isChecked);
                    break;

                case R.id.visiable_new_msg_switch:
                    PreferenceUtils.setPrefBoolean(getActivity(),
                            PreferenceConstants.TICKER, isChecked);
                    break;
                default:
                    break;
            }
        }

    }


}















