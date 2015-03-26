
package com.socialmap.yy.travelbox.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.socialmap.yy.travelbox.data.HistoryDataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryData {

    private HistoryDataBaseHelper dbhelper = null;
    private SQLiteDatabase db = null;


    public HistoryData(Context context) {
        dbhelper = HistoryDataBaseHelper.getDBhelper(context);
        db = HistoryDataBaseHelper.opDatabase();
    }


    public long addNew(History ub) {
        ContentValues values = new ContentValues();
        values.put("imageId", ub.getImageId());
        values.put("title", ub.getTitle());
        values.put("show_time", ub.getShow_time());
        values.put("local", ub.getLocal());


        return db.insert("history", null, values);

    }


    public ArrayList<HashMap<String, Object>> getUserList() {
        ArrayList<HashMap<String, Object>> al = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        Cursor c = db.query("history", null, null, null, null, null, null);
        while (c.moveToNext()) {
            int _id = c.getInt(c.getColumnIndex("_id"));
            int imageId = c.getInt(c.getColumnIndex("imageId"));
            String title = c.getString(c.getColumnIndex("title"));
            String show_time = c.getString(c.getColumnIndex("show_time"));
            String local = c.getString(c.getColumnIndex("local"));

            map = new HashMap<String, Object>();
            map.put("_id", _id + "");
            map.put("imageId", imageId + "");
            map.put("title", title);
            map.put("show_time", show_time);
            map.put("local", local);
            al.add(map);
        }

        return al;
    }


    public int updateUser(History ub) {
        ContentValues values = new ContentValues();
        values.put("imageId", ub.getImageId() + "");
        values.put("title", ub.getTitle());
        values.put("show_time", ub.getShow_time());
        values.put("local", ub.getLocal());

        return db.update("history", values, "_id=?", new String[]{ub.get_id() + ""});
    }


}

