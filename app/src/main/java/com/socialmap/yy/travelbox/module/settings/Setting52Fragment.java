package com.socialmap.yy.travelbox.module.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.service.SOSService;

/**
 * Created by gxyzw_000 on 2015/3/17.
 */
public class Setting52Fragment extends Fragment {


    private EditText limit_time;
    private EditText   sosname;
    private EditText  sosphone;
    private EditText  sossay;
    private TextView Countdown;
    private TextView StopCountdown;
    private TimeCount timeCount;
    private int limit_time_int;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.setting52fragment, container,
                false);


        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        limit_time  =  (EditText) view.findViewById(R.id.time);
        sosname = (EditText) view.findViewById(R.id.sosname);
        sosphone = (EditText) view.findViewById(R.id.sosphone);
        sossay = (EditText) view.findViewById(R.id.sossay);

        Countdown = (TextView) view.findViewById(R.id.Countdown);
        StopCountdown =  (TextView) view.findViewById(R.id.StopCountdown);

        SharedPreferences sp = getActivity().getSharedPreferences("SOStime", Context.MODE_WORLD_READABLE);

        //String content = sp.getString("time", "");
        // limit_time.setText(content);

        String content1 = sp.getString("name"," ");
        sosname.setText(content1);

        String content2 = sp.getString("phone"," ");
        sosphone.setText(content2);

        String content3 = sp.getString("say"," ");
        sossay.setText(content3);

        Log.v("倒计时", String.valueOf(limit_time_int));


        Countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(getActivity(), SOSService.class);
                Bundle bundle=new Bundle();
                bundle.putString("time", limit_time.getText().toString());
                bundle.putString("name", sosname.getText().toString());
                bundle.putString("phone", sosphone.getText().toString());
                bundle.putString("say", sossay.getText().toString());


                startIntent.putExtras(bundle);
                getActivity().startService(startIntent); // 启动服务
                limit_time_int = Integer.parseInt(limit_time.getText().toString().trim());
                timeCount = new TimeCount(limit_time_int*1000*60*60, 1000);
                timeCount.start();
            }
        });





        StopCountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent stopIntent = new Intent(getActivity(), SOSService.class);
                getActivity().stopService(stopIntent);
                timeCount.cancel();
                Countdown .setText("开始倒计时");
                Countdown.setClickable(true);
            }
        });



    }


    public void onStop() {
        super.onStop();
        //获得编辑器
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("SOStime", Context.MODE_WORLD_WRITEABLE).edit();
        //将EditText中的文本内容添加到编辑器
        //  editor.putString("time", limit_time.getText().toString());
        //提交编辑器内容
        editor.putString("name", sosname.getText().toString());
        editor.putString("phone", sosphone.getText().toString());
        editor.putString("say", sossay.getText().toString());

        editor.commit();
    }



    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            Countdown .setText("开始倒计时");
            Countdown.setClickable(true);
            Log.v("倒计时1",Countdown.getText().toString());
        }

        public void onTick(long millisUntilFinished) {
            Countdown.setClickable(false);
            Countdown.setText("在" + millisUntilFinished / (1000*60)+ "分钟后发送");
            Log.v("倒计时2",Countdown.getText().toString());

        }

    }

}





