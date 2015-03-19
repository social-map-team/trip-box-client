package com.socialmap.yy.travelbox.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.chat.switcher.Switch;
import com.socialmap.yy.travelbox.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.chat.util.PreferenceUtils;

/**
 * Created by gxyzw_000 on 2015/3/12.
 */

public  class Setting2Fragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener {

    private static Switch mNotifyRunBackgroundSwitch; //开启信息推送
    private static Switch mNewMsgSoundSwitch;//声音开启
    private static Switch mNewMsgVibratorSwitch;//震动开启
    private static Switch mVisiableNewMsgSwitch;    //消息免打扰




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

