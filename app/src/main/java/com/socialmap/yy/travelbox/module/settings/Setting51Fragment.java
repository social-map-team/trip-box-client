package com.socialmap.yy.travelbox.module.settings;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.socialmap.yy.travelbox.module.account.ContactActivity;
import com.socialmap.yy.travelbox.module.account.OperateActivity;
import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.module.chat.switcher.Switch;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceConstants;
import com.socialmap.yy.travelbox.module.chat.util.PreferenceUtils;
import com.socialmap.yy.travelbox.model.UserBeanCl;

import java.util.ArrayList;
import java.util.HashMap;


public  class Setting51Fragment extends Fragment implements
        CompoundButton.OnCheckedChangeListener {


    private  Switch SOSSwitch;
    private  ListView user_lv;
    private   GridView gv_botom_menu;
    private   UserBeanCl ubc;
    private   ArrayList<HashMap<String,String>> userlist=new ArrayList<HashMap<String,String>>();
    private  EditText et_search=null;
    private    int totalNum;//联系人总条数



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.setting51fragment, container,
                false);
        return view;
    }






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ubc=new UserBeanCl(getActivity());
        userlist=ubc.getUserList();
        this.loadListView(userlist);
        loadBotomMenu();

        gv_botom_menu=(GridView)view.findViewById(R.id.gv_botom_menu);
        user_lv=(ListView) view.findViewById(R.id.lv_userlist);
        SOSSwitch = (Switch) view .findViewById(R.id.sos_switch);
        SOSSwitch.setOnCheckedChangeListener(this);

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
                getActivity(), PreferenceConstants.SOSCHECK, true));

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sos_switch:
                PreferenceUtils.setPrefBoolean(getActivity(),
                        PreferenceConstants.SOSCHECK, isChecked);
                break;
            default:
                break;
        }
    }


    public void loadListView(ArrayList<HashMap<String,String>> userlist){
        totalNum=ubc.getTotalUserNum("");
        getActivity().setTitle("共有" + totalNum + "条记录");//查询共有多少条记录

        user_lv= (ListView)this.getActivity().findViewById(R.id.lv_userlist);
        //ArrayList<HashMap<String,String>> userlist=ubc.getUserList();

        SimpleAdapter adapter =new SimpleAdapter(getActivity(), userlist, R.layout.userlist,
                new String[]{"imageId","userName","cellphone"}, new int[]{R.id.list_item_image,R.id.first_ll_name,R.id.second_ll_phone});
        user_lv.setAdapter(adapter);
        Log.e("八哥",userlist.toString());
        //设置listView的点击事件，单击某一项，即可显示详细信息
        user_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View v, int position,
                                    long id) {

                //当单击某项时取出该项的详细信息，放入Intent中，启动下一个Activity
                HashMap map=(HashMap)parent.getItemAtPosition(position);
                Intent it=new Intent();
                it.putExtra("userInfo", map);
                //启动新的Activity
                it.setClass(getActivity(), OperateActivity.class);
                startActivityForResult(it, 3);
                //startActivity(it);
            }
        });
    }


    public void loadBotomMenu(){
        gv_botom_menu= (GridView)this.getActivity().findViewById(R.id.gv_botom_menu);

        gv_botom_menu.setNumColumns(1);//设置显示一列
        gv_botom_menu.setGravity(Gravity.CENTER);//居中显示
        gv_botom_menu.setVerticalSpacing(10);//设置垂直间隙
        gv_botom_menu.setHorizontalSpacing(10);//水平间隙

        ArrayList data=new ArrayList();
        HashMap map=new HashMap();
        map.put("imageItem", R.drawable.add);
        map.put("itemText", "添加");
        data.add(map);


        SimpleAdapter adapter=new SimpleAdapter(getActivity(), data, R.layout.set5item_menu,
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
                        it.setClass(getActivity(), ContactActivity.class);
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



