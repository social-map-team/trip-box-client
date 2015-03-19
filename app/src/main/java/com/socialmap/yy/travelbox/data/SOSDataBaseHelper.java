package com.socialmap.yy.travelbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SOSDataBaseHelper extends SQLiteOpenHelper{

	public static SOSDataBaseHelper dbhelper;
	public static SQLiteDatabase db;

	public static SOSDataBaseHelper getDBhelper(Context c){
		if(dbhelper==null){
			dbhelper=new SOSDataBaseHelper(c);
		}
		return dbhelper;
	}
	
	public static SQLiteDatabase opDatabase(){
		if(db==null){
			db=dbhelper.getWritableDatabase();
		}
		return db;
	}

	public SOSDataBaseHelper(Context context, String name, int version) {
		super(context, name,null, version);
		// TODO Auto-generated constructor stub
	}
	public SOSDataBaseHelper(Context context, String name) {
		super(context, name,null, 1);
		// TODO Auto-generated constructor stub
	}
	
	public SOSDataBaseHelper(Context context) {
		super(context, "db_contact",null, 1);
		// TODO Auto-generated constructor stub
	}

	
	public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table users(_id Integer primary key autoincrement,imageId int,userName varchar(20),cellphone varchar(20)," +
					"remark varchar(40))");
	
	}

	
	public void onUpgrade(SQLiteDatabase db, int  oldVersion, int newVersion) {

	}

}
