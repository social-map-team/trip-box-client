package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;


public class NearbyActivity extends Activity{
    private static final int XSPEED_MIN = 200;
    private static final int XDISTANCE_MIN = 150;
    private float xDown;
    private float xMove;
    private VelocityTracker mVelocityTracker;
    private List<Map<String, Object>> mData;
    private  String mainlocal = "   ";
    private  String location ;
    private  String  searchresult;
    public  EditText search;
    View view;
    Handler handler = new Handler(){
        public void handleMessage(Message paramMessage)
        {
            if(paramMessage.what == KeyEvent.KEYCODE_BACK)
            {
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);



                view = this.getLayoutInflater().inflate(R.layout.activity_nearby, null);

                setContentView(view);

                mData = NearbyData.getData();

                LayoutInflater inflater = LayoutInflater.from(this);
                LinearLayout header = (LinearLayout)inflater.inflate(R.layout.nearby_header, null);

                ListView list = (ListView)findViewById(R.id.nearbylist);
                list.addHeaderView(header);
                list.setOnItemClickListener(mOnClickListener);
                ListAdapter adapter = new MyAdapter(this);
                list.setAdapter(adapter);

         search = (EditText)findViewById(R.id.search_local);
        Button shbutton = (Button)findViewById(R.id.search_place);
        Button bxbutton = (Button)findViewById(R.id.search_view);
        Button morebutton = (Button)findViewById(R.id.search_more);

        shbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                intent.putExtra("local", "上海");
                intent.putExtra("type", "");
                startActivity(intent);
            }
        });
        bxbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                intent.putExtra("local", location);
                intent.putExtra("type", "步行街");
                startActivity(intent);
            }
        });

        morebutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                intent.putExtra("local","");
                intent.putExtra("type", "");
                startActivity(intent);
            }
        });


        search.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
                    searchresult = search.getText().toString();
                    Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                    intent.putExtra("local",location);
                    intent.putExtra("type", searchresult);
                    startActivity(intent);
                    return true;
                }
                return false;

            }
        });






//        Animation mAnimationScale =new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
//				Animation.RELATIVE_TO_SELF, 0.5f,
//				Animation.RELATIVE_TO_SELF, 0.5f);
//
//        mAnimationScale.setDuration(600);
//        view.startAnimation(mAnimationScale);

        Intent intent = this.getIntent();

      String  mainlocal = intent.getStringExtra("mainlocal");

        //location=mainlocal;

         if (mainlocal.contains("市")) {
             if(mainlocal.contains("省")){
             location = mainlocal.substring(mainlocal.indexOf("省") + 1, mainlocal.indexOf("市"));
         }
            else if(mainlocal.contains("自治区")){
                 location = mainlocal.substring(mainlocal.indexOf("区") + 1, mainlocal.indexOf("市"));
             }
             else{
                 location = mainlocal.substring(mainlocal.indexOf("addr : ") + 7, mainlocal.indexOf("市"));
             }
         }
        else{
             location = " ";

         }


            }









            private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    if (position == 1) {
                        Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                        intent.putExtra("local", location);
                        intent.putExtra("type", "美食");
                        startActivity(intent);
                    }

                        if (position == 2) {
                            Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                            intent.putExtra("local", location);
                            intent.putExtra("type", "风景");
                            startActivity(intent);
                            Log.v("蛤蛤",location);

                        }
                        if (position == 3) {
                            Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                            intent.putExtra("local", location);
                            intent.putExtra("type", "古迹");
                            startActivity(intent);
                        }


                        if (position == 4) {
                            Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                            intent.putExtra("local", location);
                            intent.putExtra("type", "艺术");
                            startActivity(intent);
                        }

                        if (position == 5) {
                            Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                            intent.putExtra("local", location);
                            intent.putExtra("type", "超市");
                            startActivity(intent);
                        }
                        if (position == 6) {
                            Intent intent = new Intent(NearbyActivity.this, NearbypoiActivity.class);
                            intent.putExtra("local", location);
                            intent.putExtra("type", "酒店");
                            startActivity(intent);
                        }


                    }

            };

            public final class ViewHolder {
                public ImageView img;
                public TextView title;
            }

            public class MyAdapter extends BaseAdapter {

                private LayoutInflater mInflater;

                public MyAdapter(Context context) {
                    this.mInflater = LayoutInflater.from(context);
                }

                @Override
                public int getCount() {
                    // TODO Auto-generated method stub
                    return mData.size();
                }

                @Override
                public Object getItem(int arg0) {
                    // TODO Auto-generated method stub
                    return null;
                }

                @Override
                public long getItemId(int arg0) {
                    // TODO Auto-generated method stub
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    ViewHolder holder = null;
                    if (convertView == null)
                    {
                        holder = new ViewHolder();

                        convertView = mInflater.inflate(R.layout.nearby_item, null);
                        convertView.setMinimumHeight(100);
                        holder.img = (ImageView) convertView.findViewById(R.id.nearby_icon);
                        holder.title = (TextView) convertView.findViewById(R.id.nearby_name);

                        convertView.setTag(holder);

                    }
                    else
                        {

                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.img.setBackgroundResource((Integer) mData.get(position).get("img"));
                    holder.title.setText((String) mData.get(position).get("title"));

                    return convertView;
                }

            }

            boolean isBack;
            public void onPause()
            {
                if (isBack)
                {
                    isBack = false;
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                super.onPause();
            }

            public boolean onKeyUp(int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_BACK)
                {
                    isBack = true;
//            Animation mAnimationScale =new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
//    				Animation.RELATIVE_TO_SELF, 0.5f,
//    				Animation.RELATIVE_TO_SELF, 0.5f);
//
//            mAnimationScale.setDuration(800);
//            view.startAnimation(mAnimationScale);
//
//    		//delay finish the activity so as to show animation
//			new Thread()
//			{
//				public void run()
//				{
//					try
//					{
//						Thread.sleep(600);
//					}
//					catch (InterruptedException e)
//					{
//						e.printStackTrace();
//					}
//
//					Message msg = new Message();
//					msg.what = KeyEvent.KEYCODE_BACK;
//					DianpingActivity.this.handler.sendMessage(msg);
//				}
//			}.start();
//
//    		 return true;
                }

                return super.onKeyUp(keyCode, event);
            }
        }

