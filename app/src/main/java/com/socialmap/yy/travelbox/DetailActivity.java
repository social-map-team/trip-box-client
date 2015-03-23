package com.socialmap.yy.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.socialmap.yy.travelbox.model.UserBean;
import com.socialmap.yy.travelbox.model.UserBeanCl;

import java.util.HashMap;

public class DetailActivity extends Activity{

	private ImageButton img_btn;
	private EditText et_name,et_phone,et_familyphone,et_workphone,et_position,et_email,et_remark;
	private Button saveOrchang,delete,back;
	boolean b=false;
	int imageId;
	HashMap map=null;
	UserBean ub=new UserBean();
	
	protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.detailinfo);
		this.setTitle("联系人详细信息");
		initWidget();
		LoadInfo();
		setEditDisable();
	}
	
	
	
	

	public void initWidget(){
		img_btn=(ImageButton)findViewById(R.id.btn_img);
		et_name=(EditText)findViewById(R.id.et_name);
		et_phone=(EditText)findViewById(R.id.et_phone);
		et_remark=(EditText)findViewById(R.id.et_remark);

		saveOrchang=(Button)findViewById(R.id.saveOrchang);
		delete=(Button)findViewById(R.id.delete);
		back=(Button)findViewById(R.id.back);
		
		



		saveOrchang.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {

				if(b==false){
					b=true;
					setEditAble();
					saveOrchang.setText("保存");
				}else if(b==true){
					b=false;
					setEditDisable();
					saveOrchang.setText("修改");
					

					
					String userName=et_name.getText().toString();
					String cellphone=et_phone.getText().toString();
					String remark=et_remark.getText().toString();
					

					ub.setImageId(DetailActivity.this.imageId);
					ub.setName(userName);
					ub.setCellphone(cellphone);
					ub.setRemark(remark);
					

					UserBeanCl ubc=new UserBeanCl(DetailActivity.this);
					int access=ubc.updateUser(ub);
					if(access==1){

						Toast.makeText(DetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						setResult(1);
						DetailActivity.this.finish();
					}else{
						Toast.makeText(DetailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
						
					}
				}
			}
		});
		
		//删除
		delete.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {


				UserBeanCl ubc=new UserBeanCl(DetailActivity.this);
				if(ubc.delete(ub.get_id())){

					Toast.makeText(DetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					setResult(1);
					DetailActivity.this.finish();
				}else{

					Toast.makeText(DetailActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
		

		back.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {

				DetailActivity.this.finish();
			}
		});
	}
	
	
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==0){
			if(resultCode==1){
				this.imageId=data.getIntExtra("imageId", R.drawable.icon);
				img_btn.setImageResource(imageId);
			}else if(resultCode==2){
				img_btn.setImageResource(imageId);
			}
		}
	}





	public void LoadInfo(){
		Intent it=getIntent();
		map=(HashMap)it.getSerializableExtra("userInfo");
		this.imageId=Integer.parseInt(map.get("imageId").toString());
		this.ub.set_id(Integer.parseInt(map.get("_id").toString()));
		img_btn.setImageResource(Integer.parseInt(map.get("imageId").toString()));
		et_name.setText(map.get("userName").toString());
		et_phone.setText(map.get("cellphone").toString());
		et_remark.setText(map.get("remark").toString());
	}
	

	public void setEditDisable(){
		img_btn.setEnabled(false);
		et_name.setEnabled(false);
		et_phone.setEnabled(false);
		et_remark.setEnabled(false);
	}
	
	public void setEditAble(){
		img_btn.setEnabled(true);
		et_name.setEnabled(true);
		et_phone.setEnabled(true);
		et_remark.setEnabled(true);
	}

}
