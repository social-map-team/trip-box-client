package com.socialmap.yy.travelbox.module.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.socialmap.yy.travelbox.module.account.ContactActivity;
import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.module.account.OperateActivity;
import com.socialmap.yy.travelbox.module.chat.switcher.Switch;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceUtils;
import com.socialmap.yy.travelbox.model.UserBeanCl;

import java.util.ArrayList;
import java.util.HashMap;


public class Setting2copyActivity  extends Activity
        {



    private static Switch SOSSwitch;
    private static ListView user_lv=null;
    private static  GridView gv_botom_menu=null;
    private static  UserBeanCl ubc=null;
    private static  ArrayList<HashMap<String,String>> userlist=new ArrayList<HashMap<String,String>>();
    private static  EditText et_search=null;
    private static   int totalNum;//联系人总条数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            //setTheme(android.R.style.Theme_Holo_Light);
            setContentView(R.layout.activity_copy_setting2);// 设置背景



        ubc=new UserBeanCl(this);
        userlist=ubc.getUserList();
        this.loadListView(userlist);
        loadBotomMenu();

        gv_botom_menu=(GridView)findViewById(R.id.gv_botom_menu);
        user_lv=(ListView) findViewById(R.id.lv_userlist);
        SOSSwitch = (Switch) findViewById(R.id.sos_switch);


        for (int i = 0; i < 10; i++) {
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("Text", "Text" + i);
            m.put("Button", "Button" + i);
            userlist.add(m);
        }


    }










        @Override
        public void onResume() {
            super.onResume();
            readData();
            userlist=ubc.getUserList();
            this.loadListView(userlist);
            loadBotomMenu();

        }


        public void readData() {

            SOSSwitch.setChecked(PreferenceUtils.getPrefBoolean(
                    this, PreferenceConstants.SOSCHECK, true));

        }



        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.sos_switch:
                    PreferenceUtils.setPrefBoolean(this,
                            PreferenceConstants.SOSCHECK, isChecked);
                    break;
                default:
                    break;
            }
        }


        public void loadListView(ArrayList<HashMap<String,String>> userlist){
            totalNum=ubc.getTotalUserNum("");
            this.setTitle("共有" + totalNum + "条记录");//查询共有多少条记录


            //ArrayList<HashMap<String,String>> userlist=ubc.getUserList();

            SimpleAdapter adapter =new SimpleAdapter(this, userlist, R.layout.userlist,
                    new String[]{"imageId","userName","cellphone"}, new int[]{R.id.list_item_image,R.id.first_ll_name,R.id.second_ll_phone});
            user_lv.setAdapter(adapter);

            //设置listView的点击事件，单击某一项，即可显示详细信息
            user_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                public void onItemClick(AdapterView<?> parent, View v, int position,
                                        long id) {

                    //当单击某项时取出该项的详细信息，放入Intent中，启动下一个Activity
                    HashMap map=(HashMap)parent.getItemAtPosition(position);
                    Intent it=new Intent();
                    it.putExtra("userInfo", map);
                    //启动新的Activity
                    it.setClass(Setting2copyActivity.this, OperateActivity.class);
                    startActivityForResult(it, 3);
                    //startActivity(it);
                }
            });
        }


        public void loadBotomMenu(){

            gv_botom_menu.setNumColumns(1);//设置显示一列
            gv_botom_menu.setGravity(Gravity.CENTER);//居中显示
            gv_botom_menu.setVerticalSpacing(10);//设置垂直间隙
            gv_botom_menu.setHorizontalSpacing(10);//水平间隙

            ArrayList data=new ArrayList();
            HashMap map=new HashMap();
            map.put("imageItem", R.drawable.add);
            map.put("itemText", "添加");
            data.add(map);


            SimpleAdapter adapter=new SimpleAdapter(this, data, R.layout.set5item_menu,
                    new String[]{"imageItem","itemText"}, new int[]{R.id.item_image,R.id.item_text});
            gv_botom_menu.setAdapter(adapter);//设置适配器列表

            //gridview的单击事件
            gv_botom_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                /**
                 * 次函数的说明信息：parent表示放在这个GridView中的数据，就是那些map
                 * view就是数据存放时的那个布局文件，position就是列的索引
                 */
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    //通过判断点击那一列来决定做什么操作
                    switch(position){
                        case 0:{
                            //表示点击了增加按钮，应跳转到增加联系人界面
                            Intent it=new Intent();
                            it.setClass(Setting2copyActivity.this, ContactActivity.class);
                            //有两种方法启动第二个Activity
                            //第一种：
                            //startActivity(it);

                            //第二种：
                            //但是这种方法需要在当前Activity中复写onActivityResult（）函数
                            startActivityForResult(it, 0);
                            break;
                        }

                    }
                }
            });
        }











        public void onActivityResult(int requestCode, int resultCode, Intent data) {

            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==0||requestCode==3){
                if(resultCode==1){
                    //表示添加或修改成功，应刷新当前页面
                    userlist=ubc.getUserList();
                    this.loadListView(userlist);
                }else if(resultCode==2){
                    //添加不成功，不刷新
                }
            }
        }






    }