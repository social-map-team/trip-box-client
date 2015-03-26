package com.socialmap.yy.travelbox.data;

/**
 * Created by gxyzw_000 on 2015/3/25.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class HistoryDataBaseHelper extends SQLiteOpenHelper {

    public static HistoryDataBaseHelper dbhelper;
    public static SQLiteDatabase db;

    public HistoryDataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);
        // TODO Auto-generated constructor stub
    }

    public HistoryDataBaseHelper(Context context, String name) {
        super(context, name, null, 1);
        // TODO Auto-generated constructor stub
    }

    public HistoryDataBaseHelper(Context context) {
        super(context, "db_history", null, 1);
        // TODO Auto-generated constructor stub
    }

    public static HistoryDataBaseHelper getDBhelper(Context c) {
        if (dbhelper == null) {
            dbhelper = new HistoryDataBaseHelper(c);
        }
        return dbhelper;
    }

    public static SQLiteDatabase opDatabase() {
        if (db == null) {
            db = dbhelper.getWritableDatabase();
        }
        return db;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(_id Integer primary key autoincrement,imageId int,title varchar(20),show_time varchar(20)," +
                "local varchar(40))");

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
