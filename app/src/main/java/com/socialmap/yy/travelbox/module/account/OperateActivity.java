package com.socialmap.yy.travelbox.module.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.socialmap.yy.travelbox.R;
import com.socialmap.yy.travelbox.model.UserBeanCl;

import java.util.ArrayList;
import java.util.HashMap;

public class OperateActivity extends Activity{

	ImageButton img_btn=null;
	TextView tv_name=null;
	ListView lv_operation=null;
	
	HashMap map=null;
	
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(android.R.style.Theme_Holo_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos_connact_operation);

		Intent it=getIntent();
		map=(HashMap)it.getSerializableExtra("userInfo");
		int imageId=Integer.parseInt(map.get("imageId").toString());
		//img_btn=(ImageButton)findViewById(R.id.img_btn);
		//img_btn.setImageResource(imageId);
		
		tv_name=(TextView)findViewById(R.id.tv_username);
		tv_name.setText(map.get("userName").toString());
		
		this.loadListView(map);
	}


	public void loadListView(HashMap map){
		ArrayList al=new ArrayList();
		HashMap hm=new HashMap();
		hm.put("img", R.drawable.call+"");
		hm.put("op_type", "呼叫");
		hm.put("number", map.get("cellphone").toString());
		al.add(hm);
		
		hm=new HashMap();
		hm.put("img", R.drawable.dx+"");
		hm.put("op_type", "发送短信");
		hm.put("number", map.get("cellphone").toString());
		al.add(hm);
		SimpleAdapter adapter =new SimpleAdapter(this, al, R.layout.sos_connact_operat,
				new String[]{"img","op_type","number"}, new int[]{R.id.op_image,R.id.op_type,R.id.number} );
		lv_operation=(ListView)findViewById(R.id.lv_operation);
		lv_operation.setAdapter(adapter);
		
		lv_operation.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {

				HashMap map=(HashMap)parent.getItemAtPosition(position);
				String mobile=map.get("number").toString();
				if(position==0){

					Intent it=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mobile));
					startActivity(it);
					
				}else if(position==1){

					Intent it=new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+mobile));
					startActivity(it);
				}
			}
		});
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, 1, 1, "编辑联系人");
		menu.add(0, 2, 2, "删除联系人");
		
		return super.onCreateOptionsMenu(menu);

	}

	
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId()==1){
			Intent it=new Intent();
			it.putExtra("userInfo", this.map);
			it.setClass(OperateActivity.this, DetailActivity.class);
			startActivity(it);
			OperateActivity.this.finish();
		}else if(item.getItemId()==2){
			UserBeanCl ubc= new UserBeanCl(OperateActivity.this);

			if(ubc.delete(Integer.parseInt(map.get("_id").toString()))){

				Toast.makeText(OperateActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				setResult(1);
				OperateActivity.this.finish();
			}else{

				Toast.makeText(OperateActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	
	
}
