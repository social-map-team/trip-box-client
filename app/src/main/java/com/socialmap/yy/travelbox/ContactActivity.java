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

public class ContactActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageButton img_btn=null;
	//private AlertDialog ImageChooseDialog=null;
	private Button save,back;
	private EditText et_name,et_phone,et_familyphone,et_workphone,et_position,et_email,et_remark;
	//private Gallery gallery=null;
	//private ImageSwitcher imageswitch=null;
	private int imageId;
	UserBeanCl ubc;

   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Holo_Light_NoActionBar);
        setContentView(R.layout.addnew);
        this.setTitle("添加联系人");
        ubc=new UserBeanCl(this);
        
        
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_remark=(EditText)findViewById(R.id.et_remark);
        img_btn=(ImageButton)findViewById(R.id.btn_img);


        

        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {


				if((et_name.getText().toString()).equals("")||(et_phone.getText().toString()).equals("")){
					Toast.makeText(ContactActivity.this, "姓名和手机不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				System.out.println(et_name.getText()+"---->");
				UserBean ub=new UserBean();
				ub.setImageId(imageId);
				ub.setName(et_name.getText().toString());
				ub.setCellphone(et_phone.getText().toString());
				ub.setRemark(et_remark.getText().toString());
				
				long access =ubc.addNew(ub);
				if(access!=-1){

					Toast.makeText(ContactActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
					setResult(1);
					ContactActivity.this.finish();
				}else{
					Toast.makeText(ContactActivity.this, "插入出错", Toast.LENGTH_SHORT).show();
					setResult(2);
					ContactActivity.this.finish();
				}
			}
		});
        
        
        back=(Button)findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {

				setResult(2);
				ContactActivity.this.finish();
			}
		});
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==0){
			if(resultCode==1){
				imageId=data.getIntExtra("imageId", R.drawable.icon);
				img_btn.setImageResource(imageId);
			}else if(resultCode==2){

			}
		}
	}
    
    
    
    
    }