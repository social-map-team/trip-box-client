package com.socialmap.yy.travelbox.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.TbsClient;

import java.io.UnsupportedEncodingException;

public  class Setting11Fragment extends Fragment {

    private static String TAG = "HelloPreference";

    private EditText username;

    private EditText realname;

    private EditText  phone;

    private EditText  birthday;

    private EditText  email;

    private TextView gender;
    private Button  submit ;

    private EditText  idcard;
    Context mContext = null;

    private String contentsex = " ";
    private int genderIndex = 0;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting11fragment, container,
                false);

        return view;

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = (EditText) view.findViewById(R.id.username1);
        realname = (EditText) view.findViewById(R.id.realname);
        phone = (EditText) view.findViewById(R.id.phone);
        idcard = (EditText) view.findViewById(R.id.idcard);
        birthday = (EditText) view.findViewById(R.id.birthday);
        email = (EditText) view.findViewById(R.id.email);
        gender = (TextView) view.findViewById(R.id.gender);
        submit = (Button) view.findViewById(R.id.submit);



        SharedPreferences sp = getActivity().getSharedPreferences("profile", Context.MODE_WORLD_READABLE);

        String content = sp.getString("username","");
        username.setText(content);

        String content1 = sp.getString("realname","");
        realname.setText(content1);

        String content2 = sp.getString("phone","");
        phone.setText(content2);


        String content3 = sp.getString("idcard","");
        idcard.setText(content3);

        String content4 = sp.getString("email","");
        email.setText(content4);

        String content5 = sp.getString("birthday","");
        birthday.setText(content5);

        String contentsex = sp.getString("gender","");
        gender.setText(contentsex);





        gender.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("请选择性别");
                final String[] sex = {"男", "女", "未知性别"};
                //    设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
                 * 第三个参数给每一个单选项绑定一个监听器
                 */
                builder.setSingleChoiceItems(sex, 2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "性别为：" + sex[which], Toast.LENGTH_SHORT).show();
                        genderIndex  = which;
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gender.setText(sex[genderIndex]);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });







        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                submit();
            }
        });
    }


    public void onStop() {
        super.onStop();
        //获得编辑器
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("profile", Context.MODE_WORLD_WRITEABLE).edit();
        //将EditText中的文本内容添加到编辑器
        editor.putString("username", username.getText().toString());
        //提交编辑器内容
        editor.putString("realname", realname.getText().toString());
        editor.putString("phone", phone.getText().toString());
        editor.putString("idcard", idcard.getText().toString());
        editor.putString("email", email.getText().toString());
        editor.putString("birthday", birthday.getText().toString());
        editor.putString("gender",  gender.getText().toString());



        editor.commit();
    }










    private void submit(){

        TbsClient.getInstance()
                .request("/api/user/register", "post",
                        "username",     username.getText(),
                        "realname",     realname.getText(),
                        "idcard",       idcard.getText(),
                        "birthday",     birthday.getText(),
                        "phone",        phone.getText(),
                        "email",        email.getText()
                )
                .execute(new TbsClient.Callback() {
                    @Override
                    public void onFinished(TbsClient.ServerResponse response) {
                        try {
                            String content = new String(response.getContent(), "UTF-8");
                            Log.i("yy", response.getStatusCode() + "\n" + content);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });




    }





    }

