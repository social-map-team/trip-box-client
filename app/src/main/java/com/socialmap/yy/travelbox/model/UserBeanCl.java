
package com.socialmap.yy.travelbox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.socialmap.yy.travelbox.data.SOSDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class UserBeanCl {

	private SOSDataBaseHelper dbhelper=null;
	private SQLiteDatabase db=null;
	

	public UserBeanCl(Context context){
		dbhelper= SOSDataBaseHelper.getDBhelper(context);
			db= SOSDataBaseHelper.opDatabase();
	}
	
	

	public int getTotalUserNum(String condition){
		int totalNum=0;
		Cursor c;
		if(condition.equals("")){
			c=db.query("users", new String[]{"count(*)"}, null, null, null, null, null);
		}else{
			c=db.query("users", new String[]{"count(*)"}, "userName like '"+condition+"%' or cellphone like '"+condition+"%'", null, null, null, null);
		}
		while(c.moveToNext()){
			totalNum=c.getInt(c.getColumnIndex("count(*)"));
		}
		return totalNum;
	}
	
	

	public long addNew(UserBean ub){
		ContentValues values=new ContentValues();
		values.put("imageId",ub.getImageId());
		values.put("userName", ub.getName());
		values.put("cellphone", ub.getCellphone());
		values.put("remark", ub.getRemark());
		

		return db.insert("users", null, values);
		 
	}
	

	public ArrayList<HashMap<String,String>> getUserList(){
		ArrayList<HashMap<String,String>> al=new ArrayList<HashMap<String,String>>();;
		HashMap<String,String> map=null;
		Cursor c=db.query("users", null, null, null, null, null, null);
		while(c.moveToNext()){
			int _id=c.getInt(c.getColumnIndex("_id"));
			int imageId=c.getInt(c.getColumnIndex("imageId"));
			String userName=c.getString(c.getColumnIndex("userName"));
			String cellphone=c.getString(c.getColumnIndex("cellphone"));
			String remark=c.getString(c.getColumnIndex("remark"));
			
			map=new HashMap<String,String>();
			map.put("_id", _id+"");
			map.put("imageId", imageId+"");
			map.put("userName", userName);
			map.put("cellphone", cellphone);
			map.put("remark", remark);
			al.add(map);
		}
		
		return al;
	}
	

	public int updateUser(UserBean ub){
		ContentValues values=new ContentValues();
		values.put("imageId",ub.getImageId()+"");
		values.put("userName", ub.getName());
		values.put("cellphone", ub.getCellphone());
		values.put("remark", ub.getRemark());
		
		return db.update("users", values, "_id=?", new String[]{ub.get_id()+""});
	}
	

	public boolean delete(int id){
		boolean b=false;
		
		int num=db.delete("users", "_id=?", new String[]{id+""});
		if(num==1){
			b=true;
		}
		return b;
	}
	
	public ArrayList<HashMap<String,String>> selectBySearch(String condition){
		ArrayList<HashMap<String,String>> al=new ArrayList<HashMap<String,String>>();;
		HashMap<String,String> map=null;
		Cursor c=db.query("users", null, "userName like '"+condition+"%' or cellphone like '"+condition+"%'", null, null, null, null);
		while(c.moveToNext()){
			int _id=c.getInt(c.getColumnIndex("_id"));
			int imageId=c.getInt(c.getColumnIndex("imageId"));
			String userName=c.getString(c.getColumnIndex("userName"));
			String cellphone=c.getString(c.getColumnIndex("cellphone"));
			String remark=c.getString(c.getColumnIndex("remark"));
			
			map=new HashMap<String,String>();
			map.put("_id", _id+"");
			map.put("imageId", imageId+"");
			map.put("userName", userName);
			map.put("cellphone", cellphone);
			map.put("remark", remark);
			al.add(map);
		}
		
		return al;
	}
	
}
