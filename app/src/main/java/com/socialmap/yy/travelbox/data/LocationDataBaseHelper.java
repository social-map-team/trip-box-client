package com.socialmap.yy.travelbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gxyzw_000 on 2015/3/26.
 */
public class LocationDataBaseHelper extends SQLiteOpenHelper {
    public static LocationDataBaseHelper dbhelper;
    public static SQLiteDatabase db;

    public LocationDataBaseHelper(Context context, String name, int version) {
        super(context, name, null, version);

    }

    public LocationDataBaseHelper(Context context, String name) {
        super(context, name, null, 1);

    }

    public LocationDataBaseHelper(Context context) {
        super(context, "db_location", null, 1);

    }

    public static LocationDataBaseHelper getDBhelper(Context c) {
        if (dbhelper == null) {
            dbhelper = new LocationDataBaseHelper(c);
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
        db.execSQL("create table users(_id Integer primary key autoincrement,imageId int,userName varchar(20),cellphone varchar(20)," +
                "remark varchar(40))");

    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
