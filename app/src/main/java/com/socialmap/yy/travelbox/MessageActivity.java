package com.socialmap.yy.travelbox;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;


public  class MessageActivity extends Activity implements View.OnTouchListener {
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    public VelocityTracker mVelocityTracker;
    private TextView myTV;


    private String[] Messagetype = new String[]{"全部","好友", "SOS", "系统", "团队", "系统" };
    private ListView MessageCheckListView;
    private boolean[] State=new boolean[]{true, false, false, false, false, false,false };



    //public Button checkBoxButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ListView list = (ListView) findViewById(R.id.list);
        myTV = (TextView) findViewById(R.id.TextView01);
     //   checkBoxButton = (Button) findViewById(R.id.checkBoxButton);


        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayOptions(actionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);




/*
        checkBoxButton.setOnClickListener(new OnClickListener() {
                                              public void onClick(View v) {
                                                  final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
                                                  AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                                                  // Set the dialog title
                                                  builder.setTitle("Pick your toppings")
                                                          // Specify the list array, the items to be selected by default (null for none),
                                                          // and the listener through which to receive callbacks when items are selected
                                                          .setMultiChoiceItems(R.array.toppings, null,
                                                                  new DialogInterface.OnMultiChoiceClickListener() {
                                                                      @Override
                                                                      public void onClick(DialogInterface dialog, int which,
                                                                                          boolean isChecked) {
                                                                          if (isChecked) {
                                                                              // If the user checked the item, add it to the selected items
                                                                              mSelectedItems.add(which);
                                                                          } else if (mSelectedItems.contains(which)) {
                                                                              // Else, if the item is already in the array, remove it
                                                                              mSelectedItems.remove(Integer.valueOf(which));
                                                                          }
                                                                      }
                                                                  })
                                                                  // Set the action buttons
                                                          .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                              @Override
                                                              public void onClick(DialogInterface dialog, int id) {
                                                                  // User clicked OK, so save the mSelectedItems results somewhere
                                                                  // or return them to the component that opened the dialog
                                                                  myTV.setText(mSelectedItems.toString());
                                                              }
                                                          })
                                                          .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                              @Override
                                                              public void onClick(DialogInterface dialog, int id) {

                                                              }
                                                          });

                                                  AlertDialog ad = builder.create();
                                                  ad.show();

                                              }
                                          }
        );


*/





        BaseAdapter adapter = new BaseAdapter() {
            private LayoutInflater inflater = getLayoutInflater();

            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.activity_message_list_item, null);
            }

            return convertView;
        }
    };
        list.setAdapter(adapter);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.setOnTouchListener(this);

    }


    @Override
    public  boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);



        getMenuInflater().inflate(R.menu.messagemenu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_message);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return true;
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.check_message:

                final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                // Set the dialog title
                builder.setTitle("Pick your toppings")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(R.array.toppings, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            mSelectedItems.add(which);
                                        } else if (mSelectedItems.contains(which)) {
                                            // Else, if the item is already in the array, remove it
                                            mSelectedItems.remove(Integer.valueOf(which));
                                        }
                                    }
                                })
                                // Set the action buttons
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                                myTV.setText(mSelectedItems.toString());
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                AlertDialog ad = builder.create();
                ad.show();




                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
}








    @Override
    public boolean onTouch(View v,MotionEvent event){
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                int xSpeed = getScrollVelocity();

                if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

}



















/*    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.message, menu);
        return true;
    }
    //主界面中菜单点击事件响应
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        case R.id.action_choose:

        break;
        case R.id.action_allread:

        break;

菜单实现比较困难
*/


/*


class  CheckBoxClickListener implements  OnClickListener{


    MessageActivity M = new MessageActivity();

    private String[] Messagetype = new String[]{"全部","好友", "SOS", "系统", "团队", "系统" };
    private ListView MessageCheckListView;
    private boolean[] State=new boolean[]{true, false, false, false, false, false,false };

    @Override
    public void onClick(View v)
                  {
        AlertDialog ad = new AlertDialog.Builder(M)
                .setTitle("选择消息类型")
                .setMultiChoiceItems(Messagetype,State,new DialogInterface.OnMultiChoiceClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton, boolean isChecked){
//点击某个区域
                    }
                }).setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int whichButton){
                        String s = "您选择了:";
                        for (int i = 0; i < Messagetype.length; i++){
                            if (MessageCheckListView.getCheckedItemPositions().get(i)){
                                s += i + ":"+ MessageCheckListView.getAdapter().getItem(i)+ " ";
                            }else{
                                MessageCheckListView.getCheckedItemPositions().get(i,false);
                            }
                        }
                        if (MessageCheckListView.getCheckedItemPositions().size() > 0){
                            Toast.makeText(M.getApplicationContext(),"默认Toast样式", Toast.LENGTH_LONG).show();
                        }else{
//没有选择
                        }
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).create();
        MessageCheckListView = ad.getListView();
        ad.show();
    }
}



*/




//单击button4按钮创建多选checkbox对话框














