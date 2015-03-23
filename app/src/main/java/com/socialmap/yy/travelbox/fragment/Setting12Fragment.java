package com.socialmap.yy.travelbox.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.TbsClient;

import java.io.UnsupportedEncodingException;

public class Setting12Fragment extends Fragment {
    EditText pass = null;
    EditText pass1 = null;
    EditText pass2 = null;
    Button button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUser();
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting12fragment, null);

         pass = (EditText) view.findViewById(R.id.password);
         pass1 = (EditText) view.findViewById(R.id.password1);
         pass2 = (EditText) view.findViewById(R.id.password2);
         button = (Button) view.findViewById(R.id.submit);

        return view;
    }

//TODO 验证之前的密码


    private void check(){


    }





    private void setUser()
    {
        if(pass!=null||pass1!=null||pass2!=null)
    {
        if (pass1.getText().toString().length() <= 0 || pass2.getText().toString().length() <= 0)

        {
            Toast.makeText(getActivity(), "密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if (!pass1.getText().toString().equals(pass2.getText().toString())) {

            Toast.makeText(getActivity(), "两次输入的密码不同", Toast.LENGTH_LONG).show();
            return;
        }

        TbsClient.getInstance(getActivity())
                .request("/api/user/register", "post",
                        "password",     pass1.getText()
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
}