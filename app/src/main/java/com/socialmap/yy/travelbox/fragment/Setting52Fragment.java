package com.socialmap.yy.travelbox.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.socialmap.yy.travelbox.R;

/**
 * Created by gxyzw_000 on 2015/3/17.
 */
public class Setting52Fragment extends Fragment {


    private EditText limit_time;
    private EditText   sosname;
    private EditText  sosphone;

    private Button Countdown;
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



        limit_time  =  (EditText) view.findViewById(R.id.username1);
        sosname = (EditText) view.findViewById(R.id.realname);
        sosphone = (EditText) view.findViewById(R.id.phone);
        Countdown = (Button) view.findViewById(R.id.Countdown);


        SharedPreferences sp = getActivity().getSharedPreferences("profile", Context.MODE_WORLD_READABLE);

        String content = sp.getString("time","");
        limit_time.setText(content);

        String content1 = sp.getString("name","");
        sosname.setText(content1);

        String content2 = sp.getString("phone","");
        sosphone.setText(content2);



        Countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                send_sos();

            }
        });
    }


    public void onStop() {
        super.onStop();
        //获得编辑器
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("profile", Context.MODE_WORLD_WRITEABLE).edit();
        //将EditText中的文本内容添加到编辑器
        editor.putString("time", limit_time.getText().toString());
        //提交编辑器内容
        editor.putString("name", sosname.getText().toString());
        editor.putString("phone", sosphone.getText().toString());



        editor.commit();
    }



    public void send_sos()
    {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 1000);


    }




}