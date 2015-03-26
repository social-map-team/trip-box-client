package com.socialmap.yy.travelbox.module.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.LocationData;
import com.socialmap.yy.travelbox.module.chat.switcher.Switch;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceUtils;
import com.socialmap.yy.travelbox.utils.TbsClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.socialmap.yy.travelbox.utils.TbsClient.getInstance;


public class LocationManagerActivity extends Activity implements
        CompoundButton.OnCheckedChangeListener {
    public Switch LocalSwitch;
    private LocationData ld;
    private ArrayList<HashMap<String, Float>> userlist = new ArrayList<HashMap<String, Float>>();
    private ListView location_lv;
    private EditText inputString;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_locationmanager);

        LocalSwitch = (Switch) findViewById(R.id.local_switch);
        LocalSwitch.setOnCheckedChangeListener(this);
        LocalSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                this, PreferenceConstants.LM, false));

        ld = new LocationData(this);
        userlist = ld.getUserList();
        this.loadListView(userlist);


    }

    @Override
    public void onResume() {
        super.onResume();
        userlist = ld.getUserList();
        this.loadListView(userlist);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.locationmanager_menu, menu);
        return true;
    }


    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.find_location:
                LayoutInflater inflater = (LayoutInflater) LocationManagerActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                RelativeLayout layout = (RelativeLayout) inflater.inflate(
                        R.layout.input_add, null);
                inputString = (EditText) layout
                        .findViewById(R.id.input_add_string);

                new AlertDialog.Builder(LocationManagerActivity.this)
                        .setTitle("请输入想要查询的好友名")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(layout)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        String str = inputString.getText().toString();

                                        getInstance()
                                                .request("/api/user", "get",
                                                        "id", str
                                                )
                                                        //todo 如何提取提取位置
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


                                });
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.local_switch:
                PreferenceUtils.setPrefBoolean(this,
                        PreferenceConstants.LM, isChecked);
                if (LocalSwitch.isChecked()) {

                    getInstance()
                            .request("/share/position", "post"
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
                break;
            default:
                break;
        }
    }


//todo 显示的应该是addr和时间以及用户名把

    public void loadListView(ArrayList<HashMap<String, Float>> userlist) {


        location_lv = (ListView) this.findViewById(R.id.lv_userlist);
        //ArrayList<HashMap<String,String>> userlist=ubc.getUserList();

        SimpleAdapter adapter = new SimpleAdapter(this, userlist, R.layout.locationlist,
                new String[]{"userName", ""}, new int[]{R.id.first_ll_username, R.id.second_ll_location});
        location_lv.setAdapter(adapter);

        //设置listView的点击事件，单击某一项，即可显示详细信息
        location_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                //当单击某项时取出该项的详细信息，放入Intent中，启动下一个Activity
                HashMap map = (HashMap) parent.getItemAtPosition(position);
                Intent it = new Intent();
                it.putExtra("location", map);
            }
        });
    }


}